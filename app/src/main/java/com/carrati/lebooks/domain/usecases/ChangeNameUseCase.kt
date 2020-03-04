package com.carrati.lebooks.domain.usecases

import com.carrati.lebooks.data.UserPreferences
import io.reactivex.Scheduler
import io.reactivex.Single

class ChangeNameUseCase(
        private val userPrefs: UserPreferences,
        private val scheduler: Scheduler
) {

    fun execute(name: String): Single<Boolean> {
        userPrefs.nome = name
        return Single.just(true).subscribeOn(scheduler)
    }
}