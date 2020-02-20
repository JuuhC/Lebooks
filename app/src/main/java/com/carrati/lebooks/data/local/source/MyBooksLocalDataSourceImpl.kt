package com.carrati.lebooks.data.local.source

import com.carrati.lebooks.data.local.database.IMyBooksDAO
import com.carrati.lebooks.data.local.mapper.MyBooksLocalMapper
import com.carrati.lebooks.domain.entities.MyBook
import io.reactivex.Single

class MyBooksLocalDataSourceImpl(private val myBooksDao: IMyBooksDAO): IMyBooksLocalDataSource {
    override fun getMyBooks(): Single<List<MyBook>> {
        return myBooksDao.getMyBooks().map{ MyBooksLocalMapper.mapFromDB( it ) }
    }

    override fun insertData(list: List<MyBook>) {
        myBooksDao.insertAll( MyBooksLocalMapper.mapToDB( list ))
    }

    override fun updateData(list: List<MyBook>) {
        myBooksDao.updateData( MyBooksLocalMapper.mapToDB( list ))
    }
}