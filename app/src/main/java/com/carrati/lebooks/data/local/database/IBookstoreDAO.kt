package com.carrati.lebooks.data.local.database

import com.carrati.lebooks.data.local.model.StoreBookLocal
import io.reactivex.Single
import androidx.room.*

@Dao
interface IBookstoreDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<StoreBookLocal>)

    @Transaction
    fun updateData(books: List<StoreBookLocal>) {
        //deleteAll()
        insertAll(books)
    }

    @Query("DELETE FROM bookstore_conf")
    fun deleteAll()

    @Query("SELECT * FROM bookstore_conf")
    fun getBookstore(): Single<List<StoreBookLocal>>

    @Delete
    fun deleteBooks(books: List<StoreBookLocal>)

    /*@Query("SELECT * FROM bookstore_conf WHERE title = :title AND writer = :writer")
    fun searchBook(title: String, writer: String)*/

    @Update
    fun favBook(vararg book: StoreBookLocal)
}