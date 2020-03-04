package com.carrati.lebooks.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carrati.lebooks.MyModuleApplication
import com.carrati.lebooks.data.local.model.StoreBookLocal
import com.carrati.lebooks.data.local.model.MyBookLocal

@Database(entities = [StoreBookLocal::class, MyBookLocal::class], version = 1, exportSchema = false) //arrayOf(StoreBookLocal::class, MyBookLocal::class))
abstract class BooksDatabase : RoomDatabase() {
    abstract fun myBooksDAO(): IMyBooksDAO
    abstract fun bookstoreDAO(): IBookstoreDAO

    companion object {
        @Volatile
        private var INSTANCE: BooksDatabase? = null

        fun getDatabase (context: Context): BooksDatabase? {
            if (this.INSTANCE != null) {
                return this.INSTANCE
            } else {
                synchronized(this) {
                    val instance = Room.databaseBuilder(context, BooksDatabase::class.java,"lebooks.db")
                            .allowMainThreadQueries()
                            .build()
                    this.INSTANCE = instance
                    return instance
                }
            }
        }
    }
}