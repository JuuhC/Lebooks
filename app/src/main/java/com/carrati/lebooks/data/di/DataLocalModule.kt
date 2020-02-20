package com.carrati.lebooks.data.di

import com.carrati.lebooks.data.local.database.BooksDatabase
import com.carrati.lebooks.data.local.source.IMyBooksLocalDataSource
import com.carrati.lebooks.data.local.source.MyBooksLocalDataSourceImpl
import com.carrati.lebooks.data.local.source.IBookstoreLocalDataSource
import com.carrati.lebooks.data.local.source.BookstoreLocalDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataModule = module {
    single { BooksDatabase.createDataBase(androidContext()) }
    factory<IMyBooksLocalDataSource> { MyBooksLocalDataSourceImpl(myBooksDao = get()) }
    factory<IBookstoreLocalDataSource> { BookstoreLocalDataSourceImpl(bookstoreDao = get()) }
}