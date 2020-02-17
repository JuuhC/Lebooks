package com.carrati.lebooks.DI

import com.carrati.lebooks.DataRepository.BookstoreRepository
import com.carrati.lebooks.DataRepository.MyBooksRepository
import com.carrati.lebooks.DataRepository.Local.MyBooksDAO
import com.carrati.lebooks.DataRepository.Remote.ServerAPI
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val moduleMyBooksRepository = module {
    factory { MyBooksRepository(get()) }
    factory { MyBooksDAO( androidContext() ) }

}

val moduleBookstoreRepository = module {
    factory { BookstoreRepository(get(), get()) }
    factory { ServerAPI() }
}

val modulesList = listOf(moduleMyBooksRepository, moduleBookstoreRepository)