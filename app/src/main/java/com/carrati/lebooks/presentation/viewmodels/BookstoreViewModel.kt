package com.carrati.lebooks.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.domain.usecases.BuyStoreBookUseCase
import com.carrati.lebooks.domain.usecases.FavorStoreBookUseCase
import com.carrati.lebooks.domain.usecases.GetStoreBooksUseCase
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.plusAssign

class BookstoreViewModel(
        val buyStoreBookUC: BuyStoreBookUseCase,
        val favorStoreBookUC: FavorStoreBookUseCase,
        val getStoreBooksUC: GetStoreBooksUseCase,
        val uiScheduler: Scheduler
) : BaseViewModel() {

    val stateList = MutableLiveData<ViewState<List<StoreBook>>>().apply {
        value = ViewState.Loading
    }

    val stateBoolean = MutableLiveData<ViewState<Boolean>>().apply {
        value = ViewState.Loading
    }

    fun getStoreBooks(forceUpdate: Boolean = false){
        disposables += getStoreBooksUC.execute(forceUpdate = forceUpdate)
                .compose(StateMachineSingle())
                .observeOn(uiScheduler)
                .subscribe(
                        {
                            stateList.postValue(it)
                        },
                        {
                        }
                )
    }

    fun onTryAgainOrReload() {
        getStoreBooks(forceUpdate = true)
    }

    fun favorStoreBook(book: StoreBook){
        disposables += favorStoreBookUC.execute(book)
                .compose(StateMachineSingle())
                .observeOn(uiScheduler)
                .subscribe(
                        {
                            stateBoolean.postValue(it)
                        },
                        {
                        }
                )
    }

    fun buyStoreBook(book: StoreBook){
        disposables += buyStoreBookUC.execute(book)
                .compose(StateMachineSingle())
                .observeOn(uiScheduler)
                .subscribe(
                        {
                            stateBoolean.postValue(it)
                        },
                        {
                        }
                )
    }
}