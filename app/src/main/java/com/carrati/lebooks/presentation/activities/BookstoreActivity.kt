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
        viewModel.buyStoreBook(book)
        var result: Boolean = false

        viewModel.stateBuyStoreBook.observe(this, Observer { state ->
            when(state) {
                is ViewState.Success -> {
                    Toast.makeText(this@BookstoreActivity, "Sucesso ao comprar livro", Toast.LENGTH_LONG).show()
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

    /*
    1- faz o download do json e joga pra dentro de um JSONArray
    2- percorre pegando os dados e criando objetos book
    3- joga os objetos dentro da lista usada pelo recyclerView
    4- notifica o adapter para recarregar a lista
     */
    /*private inner class JsonTask : AsyncTask<String, String, String>() {
        internal var pd: ProgressBar? = null

        override fun onPreExecute() { //exibe mensagem de carregando
            super.onPreExecute()

            pd = findViewById(R.id.bookstoreProgressBar)
            pd?.visibility = ProgressBar.VISIBLE
        }

        override fun doInBackground(vararg params: String): String? { //solicita o download do json

            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null

            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()


                val stream = connection.inputStream

                reader = BufferedReader(InputStreamReader(stream))

                val buffer = StringBuilder()
                var line: String? = null

                while ({ line = reader.readLine(); line}() != null) {
                    buffer.append(line + "\n")
                }

                return buffer.toString()


            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return null
        }

        override fun onPostExecute(result: String) { //pega a resposta e faz o resto
            super.onPostExecute(result)

            val booksListRaw = ArrayList<Book>()

            if (pd?.visibility == ProgressBar.VISIBLE) {
                pd?.visibility = ProgressBar.GONE
            }

            if (result.isEmpty()) {
                Toast.makeText(this@BookstoreActivity, "ERROR 1: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show()
            } else {
                val response: JSONArray
                try {
                    response = JSONArray(result)
                } catch (e: JSONException) {
                    Toast.makeText(this@BookstoreActivity, "ERROR 2: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                    return
                }

                for (i in 0 until response.length()) {
                    val obj: JSONObject
                    val book: Book
                    try {
                        obj = response.getJSONObject(i)
                        book = Book()
                        book.title = obj.getString("title")
                        book.price = obj.getInt("price")
                        book.writer = obj.getString("writer")
                        book.setThumb(obj.getString("thumbnailHd"))
                        if(myBooksDAO?.procurarLivroFavorito(book) == true) { book.setFav() }
                    } catch (e: Exception) {
                        Toast.makeText(this@BookstoreActivity, "ERROR 3: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                        return
                    }

                    //coloca na lista da loja caso não exista na lista de compras
                    if (myBooksDAO?.procurarLivroComprado(book) == false) {
                        //Log.i("booklist", "!procurarLivro");
                        booksListRaw.add(book)
                    }

                }
                booksList.addAll(booksListRaw.sortedWith(compareByDescending<Book> { it.fav }.thenBy { it.title }))
                //Log.i("size", "booklist size = " + booksList.size());
                adapter?.notifyDataSetChanged()
            }

        }
    }*/
}
