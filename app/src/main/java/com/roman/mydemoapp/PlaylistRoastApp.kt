package com.roman.mydemoapp

import android.app.Application
import com.roman.mydemoapp.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlaylistRoastApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaylistRoastApp)
            modules(appModules)
        }
    }
}
