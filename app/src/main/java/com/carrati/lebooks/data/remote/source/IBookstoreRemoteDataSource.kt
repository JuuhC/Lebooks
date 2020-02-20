package com.carrati.lebooks.data.remote.source

import com.carrati.lebooks.domain.entities.StoreBook
import io.reactivex.Single

interface IBookstoreRemoteDataSource {
    fun getBooks(): Single<List<StoreBook>>
}