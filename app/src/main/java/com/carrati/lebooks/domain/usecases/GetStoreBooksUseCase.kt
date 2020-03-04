package com.carrati.lebooks.domain.usecases

import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.domain.repository.IBookstoreRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class GetStoreBooksUseCase (
        private val repository: IBookstoreRepository,
        private val scheduler: Scheduler
) {

    fun execute(forceUpdate: Boolean): Single<List<StoreBook>> {
        return repository.getBooks(forceUpdate).subscribeOn(scheduler)
    }

}