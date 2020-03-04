package com.carrati.lebooks.presentation.viewmodels

import android.util.Log
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

    val stateBuyStoreBook = MutableLiveData<ViewState<Boolean>>().apply {
        value = ViewState.Loading
    }

    val balanceLiveData = MutableLiveData<String>().apply {
        var balance = balanceFormat(displayBalanceUC.execute())
        Log.e("Balance", balance)
        value = balance
    }

    fun getStoreBooks(forceUpdate: Boolean = false){
        disposables += getStoreBooksUC.execute(forceUpdate = forceUpdate)
                .observeOn(uiScheduler)
                .compose(StateMachineSingle())
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
                            balanceLiveData.value = balanceFormat(displayBalanceUC.execute())
                            stateBuyStoreBook.postValue(it)
                        },
                        {
                        }
                )
    }

    fun balanceFormat(balance: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(balance)
    }
}