package com.carrati.lebooks.data

import com.carrati.lebooks.data.local.source.IMyBooksLocalDataSource
import com.carrati.lebooks.domain.entities.MyBook
import com.carrati.lebooks.domain.repository.IMyBooksRepository
import io.reactivex.Single

class MyBooksRepositoryImpl(private val localDataSource: IMyBooksLocalDataSource): IMyBooksRepository{
    override fun getBooks(): Single<List<MyBook>> {
        return localDataSource.getMyBooks()
    }
}