package com.carrati.lebooks.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.carrati.lebooks.presentation.adapters.AdapterBookstore
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.R
import com.carrati.lebooks.databinding.ActivityBookstoreBinding
import com.carrati.lebooks.presentation.adapters.IRecyclerViewClickListener
import com.carrati.lebooks.presentation.viewmodels.BookstoreViewModel
import com.carrati.lebooks.presentation.viewmodels.ViewState
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

import java.util.ArrayList

class BookstoreActivity : AppCompatActivity(), IRecyclerViewClickListener {

    private val booksList = ArrayList<StoreBook>()

    private val viewModel: BookstoreViewModel by viewModel()
    private val adapter: AdapterBookstore = AdapterBookstore(booksList, this, this)

    private lateinit var binding: ActivityBookstoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookstore)
        setSupportActionBar(binding.bookstoreToolbar)
        binding.bookstoreToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.bookstoreToolbar.setNavigationOnClickListener { finish() }

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.bookstoreList.layoutManager = layoutManager
        binding.bookstoreList.setHasFixedSize(true)
        binding.bookstoreList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        getBookList(false)
    }

    fun getBookList(forceUpdate: Boolean) {
        //1- limpa a lista
        //2- executa a classe que vai pegar o json e preencher a lista

        viewModel.getStoreBooks(forceUpdate)

        viewModel.stateGetStoreBooks.observe(this, Observer { state ->
            when(state) {
                is ViewState.Success -> {
                    booksList.clear()
                    booksList.addAll(state.data
                            .sortedWith(compareByDescending<StoreBook> { it.favor }.thenBy { it.title }))
                    adapter.notifyDataSetChanged()
                    binding.bookstoreProgressBar.visibility = ProgressBar.GONE
                }
                is ViewState.Loading ->
                    binding.bookstoreProgressBar.visibility = ProgressBar.VISIBLE
                is ViewState.Failed ->
                    Toast.makeText(this@BookstoreActivity, "Erro ao carregar loja", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onClickBuyBook(book: StoreBook): Boolean {
        var result: Boolean = false  //possivelmente esta variavel nao tera seu valor alterado :)

        val bookThumb = ImageView(this@BookstoreActivity)
        Picasso.get().load(book.thumbURL).into(bookThumb)

        val builder = AlertDialog.Builder(this@BookstoreActivity)
        builder.setTitle("Deseja comprar este livro?").setMessage(book.title
                + "\n" + book.writer + "\n\n")
        builder.setView(bookThumb)

        builder.setPositiveButton("OK") { _, _ ->

            viewModel.buyStoreBook(book)

            viewModel.stateBuyStoreBook.observe(this, Observer { state ->
                when(state) {
                    is ViewState.Success -> {
                        Toast.makeText(this@BookstoreActivity, "Compra efetuada!", Toast.LENGTH_LONG).show()
                        result = true
                    }
                    is ViewState.Loading ->
                        Toast.makeText(this@BookstoreActivity, "Processando solicitação", Toast.LENGTH_SHORT).show()
                    is ViewState.Failed -> {
                        Toast.makeText(this@BookstoreActivity, "Erro ao comprar livro", Toast.LENGTH_LONG).show()
                        result = false
                    }
                }
            })
        }
        builder.setNegativeButton("Cancelar") { _, _ -> }

        builder.create().show()

        return result
    }

    override fun onClickFavBook(book: StoreBook): Boolean {
        viewModel.favorStoreBook(book)
        var result: Boolean = false

        viewModel.stateFavorStoreBook.observe(this, Observer { state ->
            when(state) {
                is ViewState.Success -> {
                    result = true
                }
                is ViewState.Loading ->
                    Toast.makeText(this@BookstoreActivity, "Processando solicitação", Toast.LENGTH_SHORT).show()
                is ViewState.Failed -> {
                    result = false
                }
            }
        })
        return result
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
            getBookList(true)
        }

        return super.onOptionsItemSelected(item)
    }
}
