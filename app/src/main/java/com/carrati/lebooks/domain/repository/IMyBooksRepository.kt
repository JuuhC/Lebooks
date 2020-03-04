package com.carrati.lebooks.domain.repository

import com.carrati.lebooks.domain.entities.MyBook
import io.reactivex.Single

interface IMyBooksRepository {
    fun getBooks(): Single<List<MyBook>>
    fun addPurchasedBook(book: MyBook)
}