package com.carrati.lebooks.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.carrati.lebooks.Config.AdapterMyBooks;
import com.carrati.lebooks.Config.UserPreferences;
import com.carrati.lebooks.Database.MyBooksDAO;
import com.carrati.lebooks.Model.Book;
import com.carrati.lebooks.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mainHello, mainSaldo;
    private AdapterMyBooks adapter;
    private List<Book> booksList = new ArrayList<>();
    private MyBooksDAO myBooksDAO;
    private UserPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        myBooksDAO = new MyBooksDAO(this);
        preferences = new UserPreferences(this);

        mainHello = findViewById(R.id.mainHello);
        mainSaldo = findViewById(R.id.mainSaldo);

        //preferences.cleanPreferences();
        //myBooksDAO.cleanBooks();
    }

    protected void onStart() {
        super.onStart();
        mainSaldo.setText("R$ " + preferences.getSaldo());
        mainHello.setText("Olá, " + preferences.getNome());
        getMyBooks();
    }

    public void getMyBooks(){
        booksList.clear();
        booksList = myBooksDAO.listar();

        /*
        1- tem que preencher o bookList antes de associar, se não o adapter perde a referencia
        2- nao precisa notificar o adapter nesse caso pois ele já vai receber a lista preenchida
         */

        RecyclerView recyclerView = findViewById(R.id.myBooksList);

        adapter = new AdapterMyBooks(booksList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapter );
    }

    //#### OK
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //#### OK
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case R.id.action_buy :
                //chama tela de bookstore
                startActivity(new Intent(this, BookstoreActivity.class));
                //getMyBooks(); ##por algum motivo não precisou
                break;
            case R.id.action_change_name :
                //chama popup de mudar nome
                changeName();
                break;
            case R.id.action_update :
                //atualiza lista
                getMyBooks();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //#### OK
    public void changeName(){ //cria uma janelinha pro usuario inserir o novo nome
        final EditText newName = new EditText(MainActivity.this);
        newName.setPadding(30, 20, 30, 20);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( MainActivity.this );
        alertDialog.setTitle("Mudar Nome");
        alertDialog.setMessage("Insira novo nome de usuário:");
        alertDialog.setCancelable(true);
        alertDialog.setView(newName);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //atualiza o shared preferences e a mainHello
                mainHello.setText("Olá, " + newName.getText());
                preferences.setNome(newName.getText().toString());
            }
        });

        alertDialog.show();
    }
}
