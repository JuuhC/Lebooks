package com.carrati.lebooks.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carrati.lebooks.data.local.model.StoreBookLocal
import com.carrati.lebooks.data.local.model.MyBookLocal

@Database(version = 1, entities = [StoreBookLocal::class, MyBookLocal::class])
abstract class BooksDatabase : RoomDatabase() {
    abstract fun myBooksDAO(): IMyBooksDAO
    abstract fun bookstoreDAO(): IBookstoreDAO

    companion object {
        var INSTANCE: BooksDatabase? = null

        fun createDataBase(context: Context): BooksDatabase? {
            if (INSTANCE == null){
                synchronized(BooksDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, BooksDatabase::class.java, "lebooks_db").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}