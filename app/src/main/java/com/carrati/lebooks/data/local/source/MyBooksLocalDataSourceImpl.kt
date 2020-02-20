package com.carrati.lebooks.data.local.source

import com.carrati.lebooks.data.local.database.IMyBooksDAO
import com.carrati.lebooks.data.local.mapper.MyBooksLocalMapper
import com.carrati.lebooks.domain.entities.MyBook
import io.reactivex.Single

class MyBooksLocalDataSourceImpl(private val myBooksDao: IMyBooksDAO): IMyBooksLocalDataSource {
    override fun getMyBooks(): Single<List<MyBook>> {
        return myBooksDao.getMyBooks().map{ MyBooksLocalMapper.mapFromDB( it ) }
    }

    override fun insertAllData(list: List<MyBook>) {
        myBooksDao.insertAll( MyBooksLocalMapper.mapToDB( list ))
    }

    override fun updateListedData(list: List<MyBook>) {
        myBooksDao.updateData( MyBooksLocalMapper.mapToDB( list ))
    }

    override fun addPurchasedBook(book: MyBook) {
        myBooksDao.insertOne( MyBooksLocalMapper.mapToDB(book) )
    }
}