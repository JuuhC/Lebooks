package com.carrati.lebooks.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.carrati.lebooks.data.old.BookstoreRepository

import com.carrati.lebooks.presentation.adapters.AdapterBookstore
import com.carrati.lebooks.presentation.adapters.IRecyclerViewBuyBookClickListener
import com.carrati.lebooks.presentation.adapters.IRecyclerViewFavClickListener
import com.carrati.lebooks.data.UserPreferences
import com.carrati.lebooks.domain.entities.Book
import com.carrati.lebooks.R
import com.squareup.picasso.Picasso

import org.koin.android.ext.android.inject

import java.util.ArrayList

class BookstoreActivity : AppCompatActivity(), IRecyclerViewBuyBookClickListener, IRecyclerViewFavClickListener {

    private var bookstoreSaldo: TextView? = null
    private var adapter: AdapterBookstore? = null
    private val booksList = ArrayList<Book>()
    private var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
    //private var myBooksDAO: MyBooksDAO? = null
    private var preferences: UserPreferences? = null
    private val bookstoreRepo: BookstoreRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookstore)
        val toolbar = findViewById<Toolbar>(R.id.bookstoreToolbar)
        setSupportActionBar(toolbar)

//      myBooksDAO = MyBooksDAO(this)
//      preferences = UserPreferences(this)

        bookstoreSaldo = findViewById(R.id.bookstoreSaldo)
        recyclerView = findViewById(R.id.bookstoreList)

        //é aqui que é definido o que o botão comprar faz
        adapter = AdapterBookstore(booksList, this, this, this)

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        bookstoreSaldo?.text = resources.getString(R.string.real) + preferences?.saldo
        getBookList()
        //por algum motivo nao foi necessario notificar o adapter apos uma compra ser realizada...
    }

    override fun onClickBuyBook(position: Int) {
        buyBook(position)
    }

    override fun onClickFavBook(position: Int) {
        favBook(position)
    }

    fun getBookList() {
        //1- limpa a lista
        //2- executa a classe que vai pegar o json e preencher a lista
        val pd: ProgressBar = findViewById(R.id.bookstoreProgressBar)
        pd?.visibility = ProgressBar.VISIBLE

        booksList.clear()

        booksList.addAll(bookstoreRepo.getBookListFromAPI()
                .sortedWith(compareByDescending<Book> { it.fav }.thenBy { it.title }))
        //getBookList()

        if (pd?.visibility == ProgressBar.VISIBLE) {
            pd?.visibility = ProgressBar.GONE
        }

        adapter?.notifyDataSetChanged()

        //JsonTask().execute("https://raw.githubusercontent.com/Felcks/desafio-mobile-lemobs/master/products.json")

    }

    fun buyBook(position: Int) {
        //1-remove da lista da loja
        //2-adiciona no banco
        //3-subtrai do saldo

        val bookThumb = ImageView(this@BookstoreActivity)
        Picasso.get().load(booksList[position].thumbURL).into(bookThumb)

        val builder = AlertDialog.Builder(this@BookstoreActivity)
        builder.setTitle("Deseja comprar este livro?").setMessage(booksList[position].title
                + "\n" + booksList[position].writer + "\n\n")
        builder.setView(bookThumb)

        //help - como faz pra transformar string em float tratando null
        builder.setPositiveButton("OK") { _, _ ->
            val saldo = preferences?.saldo ?: -1
            val price = booksList[position].price

            val result = saldo - price!!
            if (result >= 0) {
                preferences?.saldo = result //String.format("%.0f", result)
                //myBooksDAO?.salvarLivroComprado(booksList[position])
                booksList.removeAt(position)
                adapter?.notifyItemRemoved(position)
                adapter?.notifyDataSetChanged()
                bookstoreSaldo?.text = String.format("R$ ${result}")
                Toast.makeText(this@BookstoreActivity, "Compra efetuada!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@BookstoreActivity, "Saldo insuficiente", Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton("Cancelar") { _, _ -> }

        builder.create().show()
    }

    fun favBook(position: Int){
        if(booksList[position].fav){
            booksList[position].unsetFav()

            val booksListRaw = ArrayList<Book>()
            booksListRaw.addAll(booksList)

            booksList.clear()
            booksList.addAll(booksListRaw.sortedWith(compareByDescending<Book> { it.fav }.thenBy { it.title }))

            booksListRaw.clear()

            //myBooksDAO?.deletarLivroFavorito(booksList[position])
        } else {
            val livroFavoritado = booksList[position]

            livroFavoritado.setFav()

            //booksList.remove(livroFavoritado)
            //booksList.add(0, livroFavoritado)

            val booksListRaw = ArrayList<Book>()
            booksListRaw.addAll(booksList)

            booksList.clear()
            booksList.addAll(booksListRaw.sortedWith(compareByDescending<Book> { it.fav }.thenBy { it.title }))

            booksListRaw.clear()

            //myBooksDAO?.salvarLivroFavorito(livroFavoritado)
        }
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
            getBookList()
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
