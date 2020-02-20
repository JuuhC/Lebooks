package com.carrati.lebooks.data.old

import android.util.Log
import com.carrati.lebooks.data.old.db.MyBooksDAO
import com.carrati.lebooks.data.old.api.ServerAPI
import com.carrati.lebooks.domain.entities.Book
import org.json.JSONObject
import java.util.ArrayList

class BookstoreRepository(private val myBooksDAO: MyBooksDAO, private val serverAPI: ServerAPI) {

    fun getBookListFromAPI(): Iterable<Book>{
        val booksListRaw = ArrayList<Book>()

        val response = serverAPI.getJSON() ?: return booksListRaw

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
                if(myBooksDAO.procurarLivroFavorito(book)) { book.setFav() }
            } catch (e: Exception) {
                //Toast.makeText(this@BookstoreActivity, "ERROR 3: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show()
                Log.e("BookstoreRepository", "ERROR 3: JSON invalido ou sem algum ")
                e.printStackTrace()
                return booksListRaw
            }

            //coloca na lista da loja caso não exista na lista de compras
            if (!myBooksDAO?.procurarLivroComprado(book)) {
                //Log.i("booklist", "!procurarLivro");
                booksListRaw.add(book)
            }

        }

        return booksListRaw
    }


}

