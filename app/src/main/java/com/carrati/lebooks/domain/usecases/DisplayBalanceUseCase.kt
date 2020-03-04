package com.carrati.lebooks.domain.usecases

import com.carrati.lebooks.data.UserPreferences
import io.reactivex.Scheduler
import io.reactivex.Single

class DisplayBalanceUseCase(
        private val userPrefs: UserPreferences
) {

    fun execute(): Int {
        return userPrefs.saldo
    }
}