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
    }

    factory {
        ChangeNameUseCase(
                userPrefs = get(),
                scheduler = Schedulers.io()
        )
    }

    factory {
        DisplayBalanceUseCase(
                userPrefs = get()
        )
    }

    factory {
        DisplayNameUseCase(
                userPrefs = get()
        )
    }

    factory {
        FavorStoreBookUseCase(
                repository = get(),
                scheduler = Schedulers.io()
        )
    }

    factory {
        GetMyBooksUseCase(
                repository = get(),
                scheduler = Schedulers.io()
        )
    }

    factory {
        GetStoreBooksUseCase(
                repository = get(),
                scheduler = Schedulers.io()
        )
    }
}

val domainModule = listOf(useCaseModule)
