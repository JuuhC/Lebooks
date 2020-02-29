package com.carrati.lebooks.presentation.adapters

import androidx.lifecycle.MutableLiveData
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.presentation.viewmodels.ViewState

interface IRecyclerViewClickListener {

    fun onClickBuyBook(book: StoreBook): MutableLiveData<ViewState<Boolean>>
    fun onClickFavBook(book: StoreBook): MutableLiveData<ViewState<Boolean>>

}
