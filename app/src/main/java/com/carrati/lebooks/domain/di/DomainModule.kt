package com.carrati.lebooks.domain.di

import com.carrati.lebooks.domain.usecases.*
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

val useCaseModule = module {

    factory {
        BuyStoreBookUseCase(
                bookstoreRepo = get(),
                myBooksRepo = get(),
                userPrefs = get(),
                mapper = get(),
                scheduler = Schedulers.io()
        )

        ChangeNameUseCase(
                userPrefs = get(),
                scheduler = Schedulers.io()
        )

        DisplayBalanceUseCase(
                userPrefs = get()
        )

        DisplayNameUseCase(
                userPrefs = get()
        )

        FavorStoreBookUseCase(
                repository = get(),
                scheduler = Schedulers.io()
        )

        GetMyBooksUseCase(
                repository = get(),
                scheduler = Schedulers.io()
        )

        GetStoreBooksUseCase(
                repository = get(),
                scheduler = Schedulers.io()
        )
    }
}

val domainModule = listOf(useCaseModule)
