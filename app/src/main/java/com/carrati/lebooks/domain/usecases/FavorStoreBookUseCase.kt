package com.carrati.lebooks.domain.usecases

import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.domain.repository.IBookstoreRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class FavorStoreBookUseCase(
        private val repository: IBookstoreRepository,
        private val scheduler: Scheduler
) {

    fun execute(book: StoreBook): Single<Boolean>{
        return repository.favorBook(book).subscribeOn(scheduler)
    }

}