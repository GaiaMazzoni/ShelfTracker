package com.example.shelftracker

import android.app.Application
import com.example.shelftracker.utils.Notifications
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ShelfTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Notifications.initialize(this)
        startKoin {
            androidLogger()
            androidContext(this@ShelfTrackerApplication)
            modules(appModule)
        }
    }
}
