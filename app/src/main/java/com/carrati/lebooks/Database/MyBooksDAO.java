package com.carrati.lebooks.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.DatabaseUtils;

import com.carrati.lebooks.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class MyBooksDAO implements IMyBooksDAO{

    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;

    public MyBooksDAO(Context context) {
        DBHelper db = new DBHelper( context );
        writeDB = db.getWritableDatabase();
        readDB = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Book book) {
        ContentValues cv = new ContentValues();
        cv.put("title", book.getTitle() );
        cv.put("writer", book.getWriter() );
        cv.put("thumb", book.getThumbURL() );

        try {
            writeDB.insert(DBHelper.TABELA_BOOKS, null, cv );
            Log.i("INFO", "Livro salvo com sucesso!");
        }catch (Exception e){
            Log.e("INFO", "Erro ao salvar Livro " + e.getMessage() );
            return false;
        }

        return true;
    }

    @Override
    public boolean procurarLivro(Book book){
        String title = book.getTitle();
        String writer = book.getWriter();

        long count = DatabaseUtils.queryNumEntries(readDB, DBHelper.TABELA_BOOKS, "title=? AND writer=?",
                new String[]{ title, writer });

        return count >= 1;
        //Log.i("booklist", "inside procurarLivro");
    }

    @Override
    public List<Book> listar() {
        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM " + DBHelper.TABELA_BOOKS + " ;";
        Cursor c = readDB.rawQuery(sql, null);

        while ( c.moveToNext() ){

            String title = c.getString( c.getColumnIndex("title") );
            String writer = c.getString( c.getColumnIndex("writer") );
            String thumb = c.getString( c.getColumnIndex("thumb") );

            Book book = new Book();
            book.setTitle( title );
            book.setWriter( writer );
            book.setThumb( thumb );

            books.add( book );
            Log.i("myBooksDAO", book.getTitle() );
        }
        c.close();

        return books;
    }

    @Override
    public void cleanBooks(){
        String sql = "DELETE FROM " + DBHelper.TABELA_BOOKS + ";";
        writeDB.delete(DBHelper.TABELA_BOOKS, null, null);
    }
}
