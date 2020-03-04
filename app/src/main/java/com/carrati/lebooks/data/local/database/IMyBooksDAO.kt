package com.carrati.lebooks.data.local.database

import androidx.room.*
import com.carrati.lebooks.data.local.model.MyBookLocal
import io.reactivex.Single

@Dao
interface IMyBooksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(book: MyBookLocal)

    @Transaction
    fun updateData(books: List<MyBookLocal>) {
        deleteAll()
        insertAll(books)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<MyBookLocal>)

    @Query("DELETE FROM mybooks_conf")
    fun deleteAll()

    @Query("SELECT * FROM mybooks_conf")
    fun getMyBooks(): Single<List<MyBookLocal>>
}