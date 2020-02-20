package com.carrati.lebooks.data.old.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, NOME_DB, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val createTableBooks = ("CREATE TABLE IF NOT EXISTS " + TABLE_BOOKS
                + " (title TEXT PRIMARY KEY, writer TEXT NOT NULL UNIQUE, thumb TEXT NOT NULL ); ")

        try {
            db.execSQL(createTableBooks)
            Log.i("INFO DB", "Sucesso ao criar a tabela $TABLE_BOOKS")
        } catch (e: Exception) {
            Log.i("INFO DB", "Erro ao criar a tabela $TABLE_BOOKS: " + e.message)
        }

        val createTableFavs = ("CREATE TABLE IF NOT EXISTS " + TABLE_FAVS
                + " (title TEXT PRIMARY KEY, writer TEXT NOT NULL UNIQUE, price TEXT NOT NULL, thumb TEXT NOT NULL ); ")

        try {
            db.execSQL(createTableFavs)
            Log.i("INFO DB", "Sucesso ao criar a tabela $TABLE_FAVS")
        } catch (e: Exception) {
            Log.i("INFO DB", "Erro ao criar a tabela: $TABLE_FAVS" + e.message)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        val sql = "DROP TABLE IF EXISTS $TABLE_BOOKS; DROP TABLE IF EXISTS $TABLE_FAVS;"

        try {
            db.execSQL(sql)
            onCreate(db)
            Log.i("INFO DB", "Sucesso ao atualizar App")
        } catch (e: Exception) {
            Log.i("INFO DB", "Erro ao atualizar App" + e.message)
        }

    }

    companion object {
        private val VERSION = 1
        private val NOME_DB = "DB_LEBOOKS"
        var TABLE_BOOKS = "book_conf"
        var TABLE_FAVS = "favorite_conf"
    }
}

