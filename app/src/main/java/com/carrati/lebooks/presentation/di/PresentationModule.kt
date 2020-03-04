package com.carrati.lebooks.presentation.di

import com.carrati.lebooks.presentation.viewmodels.BookstoreViewModel
import com.carrati.lebooks.presentation.viewmodels.MyBooksViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val presentationModule = module {

    viewModel {
        MyBooksViewModel(
                getMyBooksUC = get(),
                changeNameUC = get(),
                displayNameUC = get(),
                displayBalanceUC = get(),
                uiScheduler = AndroidSchedulers.mainThread()
        )
    }

    viewModel {
        BookstoreViewModel(
                buyStoreBookUC = get(),
                favorStoreBookUC = get(),
                getStoreBooksUC = get(),
                displayBalanceUC = get(),
                uiScheduler = AndroidSchedulers.mainThread()
        )
    }
}