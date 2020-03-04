package com.carrati.lebooks.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.database.DatabaseUtils

import com.carrati.lebooks.Model.Book

import java.util.ArrayList

class MyBooksDAO(context: Context) : IMyBooksDAO {

    private val writeDB: SQLiteDatabase
    private val readDB: SQLiteDatabase

    init {
        val db = DBHelper(context)
        writeDB = db.writableDatabase
        readDB = db.readableDatabase
    }

    override fun salvarLivroComprado(book: Book): Boolean {
        val cv = ContentValues()
        cv.put("title", book.title)
        cv.put("writer", book.writer)
        cv.put("thumb", book.thumbURL)

        try {
            writeDB.insert(DBHelper.TABLE_BOOKS, null, cv)
            Log.i("INFO", "Livro salvo com sucesso!")
        } catch (e: Exception) {
            Log.e("INFO", "Erro ao salvar Livro " + e.message)
            return false
        }

        return true
    }

    override fun procurarLivroComprado(book: Book): Boolean {
        val title = book.title
        val writer = book.writer

        val count = DatabaseUtils.queryNumEntries(readDB, DBHelper.TABLE_BOOKS, "title=? AND writer=?",
                arrayOf(title, writer))

        return count >= 1
        //Log.i("booklist", "inside procurarLivro");
    }

    override fun listarLivroComprado(): MutableList<Book> {
        val books = ArrayList<Book>()

        val sql = "SELECT * FROM " + DBHelper.TABLE_BOOKS + " ;"
        val c = readDB.rawQuery(sql, null)

        while (c.moveToNext()) {

            val title = c.getString(c.getColumnIndex("title"))
            val writer = c.getString(c.getColumnIndex("writer"))
            val thumb = c.getString(c.getColumnIndex("thumb"))

            val book = Book()
            book.title = title
            book.writer = writer
            book.setThumb(thumb)

            books.add(book)
            Log.i("myBooksDAO", book.title)
        }
        c.close()

        return books
    }

    override fun salvarLivroFavorito(book: Book): Boolean {
        val cv = ContentValues()
        cv.put("title", book.title)
        cv.put("writer", book.writer)
        cv.put("thumb", book.thumbURL)
        cv.put("price", book.price.toString())

        try {
            writeDB.insert(DBHelper.TABLE_FAVS, null, cv)
            Log.i("INFO", "Livro salvo com sucesso!")
        } catch (e: Exception) {
            Log.e("INFO", "Erro ao salvar Livro " + e.message)
            return false
        }

        return true
    }

    override fun procurarLivroFavorito(book: Book): Boolean {
        val title = book.title
        val writer = book.writer

        val count = DatabaseUtils.queryNumEntries(readDB, DBHelper.TABLE_FAVS, "title=? AND writer=?",
                arrayOf(title, writer))

        Log.e("procurarLivro", title + " count = " + count.toString())

        return count >= 1
    }

    override fun deletarLivroFavorito(book: Book): Boolean {
        val title = book.title
        val writer = book.writer

        try {
            writeDB.delete(DBHelper.TABLE_FAVS, "title=? AND writer=?", arrayOf(title, writer))
        } catch (e: Exception) {
            Log.e("INFO", "Erro ao deletar livro dos favoritos" + e.message)
            return false
        }

        return true

    }


    override fun cleanAllBooks() {
        writeDB.delete(DBHelper.TABLE_BOOKS, null, null)
        writeDB.delete(DBHelper.TABLE_FAVS, null, null)
    }
}
