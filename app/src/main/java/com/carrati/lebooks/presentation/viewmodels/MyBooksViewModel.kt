package com.carrati.lebooks.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.carrati.lebooks.domain.entities.MyBook
import com.carrati.lebooks.domain.usecases.ChangeNameUseCase
import com.carrati.lebooks.domain.usecases.GetMyBooksUseCase
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.plusAssign

class MyBooksViewModel (
    val getMyBooksUC: GetMyBooksUseCase,
    val changeNameUC: ChangeNameUseCase,
    val uiScheduler: Scheduler
): BaseViewModel() {

    val stateList = MutableLiveData<ViewState<List<MyBook>>>().apply {
        value = ViewState.Loading
    }

    val stateBoolean = MutableLiveData<ViewState<Boolean>>().apply {
        value = ViewState.Loading
    }

    fun getMyBooks(){
        disposables += getMyBooksUC.execute()
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

    fun changeUserName(name: String){
        disposables += changeNameUC.execute(name)
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