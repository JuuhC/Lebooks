package com.carrati.lebooks.domain.usecases

import android.util.Log
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.domain.repository.IBookstoreRepository
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers

class FavorStoreBookUseCase(
        private val repository: IBookstoreRepository,
        private val scheduler: Scheduler
) {

    fun execute(book: StoreBook): Single<Boolean>{
        repository.favorBook(book)
        return Single.just(true).subscribeOn(scheduler)
    }

}