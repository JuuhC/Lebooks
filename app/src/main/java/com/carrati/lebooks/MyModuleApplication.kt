package com.carrati.lebooks

import android.app.Application
import android.app.Presentation
import com.carrati.lebooks.data.di.dataModules
import com.carrati.lebooks.domain.di.domainModule
import com.carrati.lebooks.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MyModuleApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // TIMBER LOG DEBUGGER
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger()
            androidContext(this@MyModuleApplication)
            modules(dataModules + domainModule + listOf(presentationModule))
        }
    }
}