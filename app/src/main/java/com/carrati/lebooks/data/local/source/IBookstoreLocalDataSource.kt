package com.carrati.lebooks.data.local.source

import io.reactivex.Single
import com.carrati.lebooks.domain.entities.Book
import com.carrati.lebooks.domain.entities.StoreBook

interface IBookstoreLocalDataSource {
    fun getBookList(): Single<List<StoreBook>>
    fun favBook(book: StoreBook)
    fun buyBook(book: StoreBook)

    fun insertData(list: List<StoreBook>)
    fun updateData(list: List<StoreBook>)
}