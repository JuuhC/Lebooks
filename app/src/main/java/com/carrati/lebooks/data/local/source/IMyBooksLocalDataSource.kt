package com.carrati.lebooks.data.local.source

import io.reactivex.Single
import com.carrati.lebooks.domain.entities.MyBook

interface IMyBooksLocalDataSource {
    fun getMyBooks(): Single<List<MyBook>>

    fun insertData(list: List<MyBook>)
    fun updateData(list: List<MyBook>)
}