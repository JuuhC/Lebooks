package com.carrati.lebooks.presentation.activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.carrati.lebooks.presentation.adapters.AdapterBookstore
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.R
import com.carrati.lebooks.databinding.ActivityBookstoreBinding
import com.carrati.lebooks.presentation.adapters.IRecyclerViewClickListener
import com.carrati.lebooks.presentation.viewmodels.BookstoreViewModel
import com.carrati.lebooks.presentation.viewmodels.ViewState
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CancellationException
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

import java.util.ArrayList

class BookstoreActivity : AppCompatActivity(), IRecyclerViewClickListener {

    private var searchingFlag = false

    private val viewModel: BookstoreViewModel by viewModel()
    private var adapter: AdapterBookstore? = null

    private lateinit var binding: ActivityBookstoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookstore)
        setSupportActionBar(binding.bookstoreToolbar)
        binding.bookstoreToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.bookstoreToolbar.setNavigationOnClickListener { finish() }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initAdapter(listOf())
    }

    override fun onStart() {
        super.onStart()

        getBookList(false)
        initSearchBox()
    }

    fun getBookList(forceUpdate: Boolean) {
        //1- limpa a lista
        //2- executa a classe que vai pegar o json e preencher a lista

        viewModel.stateGetStoreBooks.observe(this, Observer { state ->
            when(state) {
                is ViewState.Success -> {
                    initAdapter(state.data)
                    binding.bookstoreProgressBar.visibility = ProgressBar.GONE
                    //Log.e("Activity", state.data.toString())
                }

                is ViewState.Loading ->
                    binding.bookstoreProgressBar.visibility = ProgressBar.VISIBLE

                is ViewState.Failed -> {
                    binding.bookstoreProgressBar.visibility = ProgressBar.GONE

                    Toast.makeText(this@BookstoreActivity, "Erro ao carregar loja", Toast.LENGTH_SHORT).show()

                    Log.e("Activity", state.throwable.toString())
                }
            }
        })

        Timber.e(forceUpdate.toString())
        binding.bookstoreProgressBar.visibility = ProgressBar.VISIBLE
        viewModel.getStoreBooks(forceUpdate)
    }

    fun initAdapter(list: List<StoreBook>){
        if(adapter == null){
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            binding.bookstoreList.layoutManager = layoutManager
            binding.bookstoreList.setHasFixedSize(true)

            adapter = AdapterBookstore(list, this, applicationContext, this)
            binding.bookstoreList.adapter = adapter
        } else {
            adapter!!.updateAllItens(list)
        }
    }

    fun initSearchBox(){
        val searchTxt = binding.searchTxt
        val searchImg = binding.searchImg
        val manager = searchTxt.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        searchTxt.inputType = InputType.TYPE_CLASS_TEXT

        //define o listener pra quando o texto for modificado
        searchTxt.addTextChangedListener( object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                adapter?.searchBook(searchTxt.text.toString())

                if(searchTxt.text.isNotEmpty()){
                    searchImg.setBackgroundResource(R.drawable.ic_cancel_gray)
                }
                else{
                    searchImg.setBackgroundResource(R.drawable.ic_magn_glass)
                }
                searchingFlag = searchTxt.text.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        //define o listener para quando clicar na lupa
        searchImg.setOnClickListener {
            if(searchingFlag) cleanSearch()

            //esconder teclado
            manager.hideSoftInputFromWindow(searchTxt.windowToken, 0)
        }

        searchTxt.setOnKeyListener { _, keyCode, event ->
            if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                manager.hideSoftInputFromWindow(searchTxt.windowToken, 0)
            }
            false
        }
    }

    fun cleanSearch(){
        binding.searchTxt.text.clear()
    }

    override fun onClickBuyBook(book: StoreBook): MutableLiveData<ViewState<Boolean>> {
        val bookThumb = ImageView(this@BookstoreActivity)
        Picasso.get().load(book.thumbURL).into(bookThumb)

        val builder = AlertDialog.Builder(this@BookstoreActivity)
        builder.setTitle("Deseja comprar este livro?").setMessage(book.title
                + "\n" + book.writer + "\n\n")
        builder.setView(bookThumb)

        builder.setPositiveButton("OK") { _, _ ->
            viewModel.buyStoreBook(book)
        }
        builder.setNegativeButton("Cancelar") { _, _ ->
            viewModel.stateBuyStoreBook.value = ViewState.Failed(CancellationException())
        }

        builder.create().show()

        return viewModel.stateBuyStoreBook
    }

    override fun onClickFavBook(book: StoreBook): MutableLiveData<ViewState<Boolean>> {
        viewModel.favorStoreBook(book)
        return viewModel.stateFavorStoreBook
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_bookstore, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_reload) {
            cleanSearch()
            getBookList(true)
        }

        return super.onOptionsItemSelected(item)
    }
}
