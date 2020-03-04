package com.carrati.lebooks.data.local.source

import io.reactivex.Single
import com.carrati.lebooks.domain.entities.MyBook

interface IMyBooksLocalDataSource {
    fun getMyBooks(): Single<List<MyBook>>
    fun addPurchasedBook(book: MyBook)

    fun insertAllData(list: List<MyBook>)
    fun updateListedData(list: List<MyBook>)
}