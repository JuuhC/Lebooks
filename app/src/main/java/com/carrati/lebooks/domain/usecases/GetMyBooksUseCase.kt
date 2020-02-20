package com.carrati.lebooks.domain.usecases

import com.carrati.lebooks.domain.entities.MyBook
import com.carrati.lebooks.domain.repository.IMyBooksRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class GetMyBooksUseCase(
        private val repository: IMyBooksRepository,
        private val scheduler: Scheduler
) {
    fun execute(): Single<List<MyBook>> {
        return repository.getBooks().subscribeOn(scheduler)
    }
}