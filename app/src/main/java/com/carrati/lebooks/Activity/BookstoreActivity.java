package com.carrati.lebooks.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.carrati.lebooks.Config.AdapterBookstore;
import com.carrati.lebooks.Config.IRecyclerViewClickListener;
import com.carrati.lebooks.Config.UserPreferences;
import com.carrati.lebooks.Database.MyBooksDAO;
import com.carrati.lebooks.Model.Book;
import com.carrati.lebooks.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookstoreActivity extends AppCompatActivity {

    private TextView bookstoreSaldo;
    private AdapterBookstore adapter;
    private List<Book> booksList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyBooksDAO myBooksDAO;
    private UserPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookstore);
        Toolbar toolbar = findViewById(R.id.bookstoreToolbar);
        setSupportActionBar(toolbar);

        myBooksDAO = new MyBooksDAO(this);
        preferences = new UserPreferences(this);

        bookstoreSaldo = findViewById(R.id.bookstoreSaldo);
        recyclerView = findViewById(R.id.bookstoreList);

        //é aqui que é definido o que o botão comprar faz
        adapter = new AdapterBookstore(booksList, new IRecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                buyBook(position);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapter );

    }

    @Override
    protected void onStart() {
        super.onStart();
        bookstoreSaldo.setText("R$ " + preferences.getSaldo());
        getBookList();
        //por algum motivo nao foi necessario notificar o adapter apos uma compra ser realizada...
    }

    public void getBookList(){
        //1- limpa a lista
        //2- executa a classe que vai pegar o json e preencher a lista
        booksList.clear();
        new JsonTask().execute("https://raw.githubusercontent.com/Felcks/desafio-mobile-lemobs/master/products.json");
    }

    public void buyBook(final int position){
        //1-remove da lista da loja
        //2-adiciona no banco
        //3-subtrai do saldo

        final ImageView bookThumb = new ImageView(BookstoreActivity.this);
        Picasso.get().load(booksList.get(position).getThumbURL()).into(bookThumb);

        final AlertDialog.Builder builder = new AlertDialog.Builder(BookstoreActivity.this);
        builder.setTitle("Deseja comprar este livro?").setMessage(booksList.get(position).getTitle()
                + "\n" + booksList.get(position).getWriter() + "\n\n");
        builder.setView(bookThumb);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float saldo = Float.valueOf(preferences.getSaldo());
                float price = Float.valueOf(booksList.get(position).getPrice());
                float res = saldo - price;
                if(res >= 0) {
                    preferences.setSaldo(String.format("%.0f", res));
                    myBooksDAO.salvar(booksList.get(position));
                    booksList.remove(position);
                    adapter.notifyItemRemoved(position);
                    bookstoreSaldo.setText(String.format("R$ %.0f", res));
                    Toast.makeText(BookstoreActivity.this, "Compra efetuada!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BookstoreActivity.this, "Saldo insuficiente", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bookstore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reload) {
            getBookList();
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    1- faz o download do json e joga pra dentro de um JSONArray
    2- percorre pegando os dados e criando objetos book
    3- joga os objetos dentro da lista usada pelo recyclerView
    4- notifica o adapter para recarregar a lista
     */
    private class JsonTask extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        protected void onPreExecute() { //exibe mensagem de carregando
            super.onPreExecute();

            pd = new ProgressDialog(BookstoreActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) { //solicita o download do json

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) { //pega a resposta e faz o resto
            super.onPostExecute(result);

            if (pd.isShowing()){
                pd.dismiss();
            }

            if(result.isEmpty()){
                Toast.makeText(BookstoreActivity.this, "ERROR 1: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show();
            } else {
                JSONArray response;
                try {
                    response = new JSONArray(result);
                } catch (JSONException e) {
                    Toast.makeText(BookstoreActivity.this, "ERROR 2: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return;
                }
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj;
                    Book book;
                    try {
                        obj = response.getJSONObject(i);
                        book = new Book();
                        book.setTitle(obj.getString("title"));
                        book.setPrice(obj.getString("price"));
                        book.setWriter(obj.getString("writer"));
                        book.setThumb(obj.getString("thumbnailHd"));
                    } catch (JSONException e) {
                        Toast.makeText(BookstoreActivity.this, "ERROR 3: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        return;
                    }
                    //coloca na lista da loja caso não exista na lista de compras
                    if (!myBooksDAO.procurarLivro(book)) {
                        //Log.i("booklist", "!procurarLivro");
                        booksList.add(book);
                    }
                }
                //Log.i("size", "booklist size = " + booksList.size());
                adapter.notifyDataSetChanged();
            }

        }
    }
}
