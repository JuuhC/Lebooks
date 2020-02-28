package com.carrati.lebooks.data

import android.util.Log
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
            localDataSource.getBookList().flatMap{ listBooks ->
                        when {
                            listBooks.isEmpty() -> {
                                Log.e("Repo", "listBooks is empty")
                                getBooksRemote(false)
                            }
                            else -> {
                                Log.e("Repo", "listBooks isn't empty")
                                Single.just(listBooks)
                            }
                        }
                    }
    }

    private fun getBooksRemote(isUpdate: Boolean): Single<List<StoreBook>> {
        return remoteDataSource.getBooks().flatMap { listBooks ->
            if(isUpdate)
                localDataSource.updateData( listBooks )
            else {
                Log.e("Repo", "insertData")
                localDataSource.insertData(listBooks)
            }
            Single.just( listBooks )
        }
    }

    override fun buyBook(book: StoreBook): Single<Boolean> {
        localDataSource.favBook(book)
        return Single.just(true)
    }

    override fun favorBook(book: StoreBook): Single<Boolean> {
        localDataSource.favBook(book)
        return Single.just(true)
    }
}