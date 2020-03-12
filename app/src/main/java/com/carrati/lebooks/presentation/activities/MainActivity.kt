package com.carrati.lebooks.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.carrati.lebooks.presentation.adapters.AdapterMyBooks
import com.carrati.lebooks.R
import com.carrati.lebooks.databinding.ActivityMainBinding
import com.carrati.lebooks.domain.entities.MyBook
import com.carrati.lebooks.presentation.viewmodels.MyBooksViewModel
import com.carrati.lebooks.presentation.viewmodels.ViewState
import kotlinx.android.synthetic.main.change_name.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val booksList: MutableList<MyBook> = mutableListOf()

    private val viewModel: MyBooksViewModel by viewModel()
    private val adapter: AdapterMyBooks = AdapterMyBooks(booksList)

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.mainToolbar)

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.myBooksList.layoutManager = layoutManager
        binding.myBooksList.setHasFixedSize(true)
        binding.myBooksList.adapter = adapter

        //preferences?.cleanPreferences();
        //myBooksDAO.cleanAllBooks();

        binding.bookstoreFab.setOnClickListener {
            startActivity(Intent(this, BookstoreActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateBalance()
        getBookList()

        viewModel.stateGetMyBooks.observe(this, Observer { state ->
            when(state) {
                is ViewState.Success -> {
                    booksList.clear()
                    booksList.addAll(state.data
                            .sortedWith(compareByDescending { it.title }))
                    adapter.notifyDataSetChanged()
                    binding.mainProgressBar.visibility = ProgressBar.GONE
                }
                is ViewState.Loading ->
                    binding.mainProgressBar.visibility = ProgressBar.VISIBLE
                is ViewState.Failed ->
                    Toast.makeText(this@MainActivity, "Erro ao carregar livros", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getBookList() {
        //1- limpa a lista
        //2- executa a classe que vai pegar do banco e preenche a lista

        viewModel.getMyBooks()
    }

    //#### OK
    fun changeName() { //cria uma janelinha pro usuario inserir o novo nome
        val view = LayoutInflater.from(this).inflate(R.layout.change_name, null)
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle("Mudar Nome")
        alertDialog.setMessage("Insira novo nome de usuário:")
        alertDialog.setCancelable(true)
        alertDialog.setView(view)

        alertDialog.setPositiveButton("OK") { _, _ ->
            //atualiza o shared preferences e a mainHello
            viewModel.changeUserName(view.newName.text.toString())
        }

        alertDialog.setNegativeButton("Cancelar") {_, _ -> }

        alertDialog.show()
    }

    //#### OK
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //#### OK
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        when (item.itemId) {
            R.id.action_buy -> {
                //chama tela de bookstore
                startActivity(Intent(this, BookstoreActivity::class.java))
            }
            R.id.action_change_name ->
                //chama popup de mudar nome
                changeName()
        }

        return super.onOptionsItemSelected(item)
    }
}
