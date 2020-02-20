package com.carrati.lebooks.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import com.carrati.lebooks.data.local.model.StoreBookLocal
import io.reactivex.Single

@Dao
interface IBookstoreDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(storeBook: List<StoreBookLocal>)

    @Transaction
    fun updateData(storeBook: List<StoreBookLocal>) {
        deleteAll()
        insertAll(storeBook)
    }

    @Query("DELETE FROM bookstore_conf")
    fun deleteAll()

    @Query("SELECT * FROM bookstore_conf")
    fun getBookstore(): Single<List<StoreBookLocal>>
}