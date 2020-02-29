package com.carrati.lebooks.data.remote.source

import android.util.Log
import com.carrati.lebooks.data.remote.api.IServerAPI
import com.carrati.lebooks.data.remote.mapper.StoreBookRemoteMapper
import com.carrati.lebooks.domain.entities.StoreBook
import io.reactivex.Single

class BookstoreRemoteDataSourceImpl(private val serverAPI: IServerAPI): IBookstoreRemoteDataSource {

    override fun getBooks(): Single<List<StoreBook>> {
        Log.e("DataSource", "getBooks")
        if(serverAPI.fetchBooks() == null){
            Log.e("DataSource", "fetchBooks is null")
        } else {
            Log.e("DataSource", "fetchBooks isn't null")
        }
        StoreBookRemoteMapper.id = 1
        return serverAPI.fetchBooks().map{ StoreBookRemoteMapper.mapFromAPI(it) }
    }
}