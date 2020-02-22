package com.carrati.lebooks.domain.usecases

import com.carrati.lebooks.data.UserPreferences
import io.reactivex.Scheduler
import io.reactivex.Single

class DisplayBalanceUseCase(
        private val userPrefs: UserPreferences,
        private val scheduler: Scheduler
) {

    fun execute(): Single<Int> {
        return Single.just(userPrefs.saldo).subscribeOn(scheduler)
    }
}