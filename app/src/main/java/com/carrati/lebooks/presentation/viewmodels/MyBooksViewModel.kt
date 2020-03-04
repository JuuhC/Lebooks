package com.carrati.lebooks.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.carrati.lebooks.domain.entities.MyBook
import com.carrati.lebooks.domain.usecases.ChangeNameUseCase
import com.carrati.lebooks.domain.usecases.DisplayBalanceUseCase
import com.carrati.lebooks.domain.usecases.DisplayNameUseCase
import com.carrati.lebooks.domain.usecases.GetMyBooksUseCase
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.plusAssign
import java.text.NumberFormat
import java.util.*

class MyBooksViewModel (
    val getMyBooksUC: GetMyBooksUseCase,
    val changeNameUC: ChangeNameUseCase,
    val displayNameUC: DisplayNameUseCase,
    val displayBalanceUC: DisplayBalanceUseCase,
    val uiScheduler: Scheduler
): BaseViewModel() {

    val stateGetMyBooks = MutableLiveData<ViewState<List<MyBook>>>().apply {
        value = ViewState.Loading
    }

    val stateChangeName = MutableLiveData<ViewState<Boolean>>().apply {
        value = ViewState.Loading
    }

    val balanceLiveData = MutableLiveData<String>().apply {
        var balance = displayBalanceUC.execute()
        value = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(balance)
    }

    val usernameLiveData = MutableLiveData<String>().apply {
        value = "Olá, " + displayNameUC.execute()
    }

    fun getMyBooks(){
        disposables += getMyBooksUC.execute()
                .compose(StateMachineSingle())
                .observeOn(uiScheduler)
                .subscribe(
                        {
                            stateGetMyBooks.postValue(it)
                        },
                        {
                        }
                )
    }

    fun changeUserName(name: String){
        disposables += changeNameUC.execute(name)
                .compose(StateMachineSingle())
                .observeOn(uiScheduler)
                .subscribe(
                        {
                            usernameLiveData.value = "Olá, " + displayNameUC.execute()
                            stateChangeName.postValue(it)
                        },
                        {
                        }
                )
    }

    fun updateBalance(){
        var balance = displayBalanceUC.execute()
        balanceLiveData.value = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(balance)
    }
}