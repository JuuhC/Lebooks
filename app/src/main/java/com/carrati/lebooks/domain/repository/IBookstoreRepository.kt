package com.carrati.lebooks.domain.repository

import com.carrati.lebooks.domain.entities.StoreBook
import io.reactivex.Single

interface IBookstoreRepository {
    fun getBooks(forceUpdate: Boolean): Single<List<StoreBook>>
}