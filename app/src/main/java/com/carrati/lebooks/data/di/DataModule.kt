package com.carrati.lebooks.data.di

import com.carrati.lebooks.data.BookstoreRepositoryImpl
import com.carrati.lebooks.data.MyBooksRepositoryImpl
import com.carrati.lebooks.data.UserPreferences
import com.carrati.lebooks.domain.repository.IBookstoreRepository
import com.carrati.lebooks.domain.repository.IMyBooksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<IBookstoreRepository> {
        BookstoreRepositoryImpl(
                localDataSource = get(),
                remoteDataSource = get()
        )
    }
    factory<IMyBooksRepository> {
        MyBooksRepositoryImpl(
                localDataSource = get()
        )
    }
    factory {
       UserPreferences(
               context = androidContext()
       )
    }
}

val dataModules = listOf(remoteDataModule, repositoryModule, localDataModule)