package com.carrati.lebooks.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView

import com.carrati.lebooks.Config.AdapterMyBooks
import com.carrati.lebooks.Config.UserPreferences
import com.carrati.lebooks.Database.MyBooksDAO
import com.carrati.lebooks.Model.Book
import com.carrati.lebooks.R

class MainActivity : AppCompatActivity() {

    private var mainHello: TextView? = null
    private var mainSaldo: TextView? = null
    private var adapter: AdapterMyBooks? = null
    private var booksList: MutableList<Book> = mutableListOf()
    private lateinit var myBooksDAO: MyBooksDAO
    private var preferences: UserPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        myBooksDAO = MyBooksDAO(this)
        preferences = UserPreferences(this)

        mainHello = findViewById(R.id.mainHello)
        mainSaldo = findViewById(R.id.mainSaldo)

        //preferences?.cleanPreferences();
        //myBooksDAO.cleanAllBooks();
    }

    override fun onStart() {
        super.onStart()

        mainSaldo?.text = resources.getString(R.string.real) + preferences?.saldo;
        mainHello?.text = "Olá, " + preferences?.nome;
        getMyBooks()
    }

    fun getMyBooks() {
        booksList.clear()
        booksList = myBooksDAO.listarLivroComprado()

        /*
        1- tem que preencher o bookList antes de associar, se não o adapter perde a referencia
        2- nao precisa notificar o adapter nesse caso pois ele já vai receber a lista preenchida
         */

        val recyclerView = findViewById<RecyclerView>(R.id.myBooksList)

        adapter = AdapterMyBooks(booksList)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
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
            R.id.action_buy ->
                //chama tela de bookstore
                startActivity(Intent(this, BookstoreActivity::class.java))
            R.id.action_change_name ->
                //chama popup de mudar nome
                changeName()
            R.id.action_update ->
                //atualiza lista
                getMyBooks()
        }//getMyBooks(); ##por algum motivo não precisou

        return super.onOptionsItemSelected(item)
    }

    //#### OK
    fun changeName() { //cria uma janelinha pro usuario inserir o novo nome
        val newName = EditText(this@MainActivity)
        newName.setPadding(30, 20, 30, 20)
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle("Mudar Nome")
        alertDialog.setMessage("Insira novo nome de usuário:")
        alertDialog.setCancelable(true)
        alertDialog.setView(newName)

        alertDialog.setPositiveButton("OK") { _, _ ->
            //atualiza o shared preferences e a mainHello
            mainHello?.text = "Olá, " + newName.text
            preferences?.nome = newName.text.toString()
        }

        alertDialog.show()
    }
}
