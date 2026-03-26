package com.wilson.focusmode

import android.app.Application
import com.wilson.focusmode.core.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application(){
    override fun onCreate() {
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }
        super.onCreate()
    }
}