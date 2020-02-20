package com.carrati.lebooks.data.local.source

import io.reactivex.Single
import com.carrati.lebooks.domain.entities.Book
import com.carrati.lebooks.domain.entities.StoreBook

interface IBookstoreLocalDataSource {
    fun getBookstore(): Single<List<StoreBook>>

    fun insertData(list: List<StoreBook>)
    fun updateData(list: List<StoreBook>)
}