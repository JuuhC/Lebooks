package com.carrati.lebooks.data.remote.source

import com.carrati.lebooks.data.remote.api.IServerAPI
import com.carrati.lebooks.data.remote.mapper.StoreBookRemoteMapper
import com.carrati.lebooks.domain.entities.StoreBook
import io.reactivex.Single

class BookstoreRemoteDataSourceImpl(private val serverAPI: IServerAPI): IBookstoreRemoteDataSource {

    override fun getBooks(): Single<List<StoreBook>> {
        return serverAPI.fetchBooks().map{ StoreBookRemoteMapper.mapFromAPI(it) }
    }
}