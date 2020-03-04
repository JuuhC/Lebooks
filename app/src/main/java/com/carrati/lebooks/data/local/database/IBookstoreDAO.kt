package com.carrati.lebooks.data.local.database

import com.carrati.lebooks.data.local.model.StoreBookLocal
import io.reactivex.Single
import androidx.room.*

@Dao
interface IBookstoreDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<StoreBookLocal>)

    /*@Transaction
    fun updateData(books: List<StoreBookLocal>) {
        deleteAll()
        insertAll(books)
    }*/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun updateData(books: List<StoreBookLocal>)

    @Query("DELETE FROM bookstore_conf")
    fun deleteAll()

    @Query("SELECT a.id, a.title, a.writer, a.thumb_url, a.price, a.favor" +
            " FROM bookstore_conf as a LEFT JOIN mybooks_conf as b" +
            " ON a.id = b.id WHERE b.id IS NULL")
    fun getBookstore(): Single<List<StoreBookLocal>>

    @Delete
    fun deleteBook(book: StoreBookLocal)

    /*@Query("SELECT * FROM bookstore_conf WHERE title = :title AND writer = :writer")
    fun searchBook(title: String, writer: String)*/

    //@Query("UPDATE bookstore_conf SET favor = :favor")
    //fun favBook(favor: Boolean)

    @Update
    fun favBook(book: StoreBookLocal)

}