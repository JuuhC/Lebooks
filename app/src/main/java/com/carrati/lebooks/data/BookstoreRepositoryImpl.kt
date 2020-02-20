package com.carrati.lebooks.data

import com.carrati.lebooks.data.local.source.IBookstoreLocalDataSource
import com.carrati.lebooks.data.remote.source.IBookstoreRemoteDataSource
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.domain.repository.IBookstoreRepository
import io.reactivex.Single

class BookstoreRepositoryImpl(
        private val localDataSource: IBookstoreLocalDataSource,
        private val remoteDataSource: IBookstoreRemoteDataSource
): IBookstoreRepository {

    override fun getBooks(forceUpdate: Boolean): Single<List<StoreBook>> {
        return if (forceUpdate)
            getBooksRemote(forceUpdate)
        else
            localDataSource.getBookstore().flatMap{ listBooks ->
                        when {
                            listBooks.isEmpty() -> getBooksRemote(false)
                            else -> Single.just(listBooks)
                        }
                    }
    }

    private fun getBooksRemote(isUpdate: Boolean): Single<List<StoreBook>> {
        return remoteDataSource.getBooks().flatMap { listBooks ->
            if(isUpdate)
                localDataSource.updateData( listBooks )
            else
                localDataSource.insertData( listBooks )
            Single.just( listBooks )
        }
    }
}