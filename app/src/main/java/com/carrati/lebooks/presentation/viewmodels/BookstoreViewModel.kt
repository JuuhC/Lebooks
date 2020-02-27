package com.carrati.lebooks.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.domain.usecases.BuyStoreBookUseCase
import com.carrati.lebooks.domain.usecases.DisplayBalanceUseCase
import com.carrati.lebooks.domain.usecases.FavorStoreBookUseCase
import com.carrati.lebooks.domain.usecases.GetStoreBooksUseCase
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.plusAssign
import java.text.NumberFormat
import java.util.*

class BookstoreViewModel(
        val buyStoreBookUC: BuyStoreBookUseCase,
        val favorStoreBookUC: FavorStoreBookUseCase,
        val getStoreBooksUC: GetStoreBooksUseCase,
        val displayBalanceUC: DisplayBalanceUseCase,
        val uiScheduler: Scheduler
) : BaseViewModel() {

    val stateGetStoreBooks = MutableLiveData<ViewState<List<StoreBook>>>().apply {
        value = ViewState.Loading
    }

    val stateFavorStoreBook = MutableLiveData<ViewState<Boolean>>().apply {
        value = ViewState.Loading
    }

    val stateBuyStoreBook = MutableLiveData<ViewState<Int>>().apply {
        value = ViewState.Loading
    }

    fun getStoreBooks(forceUpdate: Boolean = false){
        disposables += getStoreBooksUC.execute(forceUpdate = forceUpdate)
                .compose(StateMachineSingle())
                .observeOn(uiScheduler)
                .subscribe(
                        {
                            stateGetStoreBooks.postValue(it)
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
                            stateFavorStoreBook.postValue(it)
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
                            stateBuyStoreBook.postValue(it)
                        },
                        {
                        }
                )
    }

    fun displayBalance(): String{

        var balance = displayBalanceUC.execute()
        val formatedBalance = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(balance)

        return formatedBalance
    }
}