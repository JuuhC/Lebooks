package com.carrati.lebooks.data.local.database

import androidx.room.*
import com.carrati.lebooks.data.local.model.MyBookLocal
import io.reactivex.Single

@Dao
interface IMyBooksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(mybooks: List<MyBookLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(mybooks: MyBookLocal)

    @Transaction
    fun updateData(mybooks: List<MyBookLocal>) {
        deleteAll()
        insertAll(mybooks)
    }

    @Query("DELETE FROM mybooks_conf")
    fun deleteAll()

    @Query("SELECT * FROM mybooks_conf")
    fun getMyBooks(): Single<List<MyBookLocal>>
}