package com.carrati.lebooks.data.local.source

import com.carrati.lebooks.data.local.database.IBookstoreDAO
import com.carrati.lebooks.data.local.mapper.BookstoreLocalMapper
import com.carrati.lebooks.domain.entities.StoreBook
import io.reactivex.Single

class BookstoreLocalDataSourceImpl(private val bookstoreDao: IBookstoreDAO): IBookstoreLocalDataSource {

    override fun getBookstore(): Single<List<StoreBook>> {
        return bookstoreDao.getBookstore()
                .map { BookstoreLocalMapper.mapFromDB(it) }
    }

    override fun insertData(list: List<StoreBook>) {
        bookstoreDao.insertAll(BookstoreLocalMapper.mapToDB(list))
    }

    override fun updateData(list: List<StoreBook>) {
        bookstoreDao.updateData(BookstoreLocalMapper.mapToDB(list))
    }
}