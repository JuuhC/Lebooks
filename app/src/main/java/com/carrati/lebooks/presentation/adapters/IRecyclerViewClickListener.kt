package com.carrati.lebooks.presentation.adapters

import com.carrati.lebooks.domain.entities.StoreBook

interface IRecyclerViewClickListener {

    fun onClickBuyBook(book: StoreBook): Boolean
    fun onClickFavBook(book: StoreBook): Boolean

}
