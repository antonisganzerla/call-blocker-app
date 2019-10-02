package com.sgztech.callblocker.core

import android.app.Application
import com.sgztech.callblocker.di.dbModule
import com.sgztech.callblocker.di.repositoryModule
import com.sgztech.callblocker.di.uiModule
import com.sgztech.callblocker.util.NotificationUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class CoreApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoreApplication)
            modules(listOf(dbModule, repositoryModule, uiModule))
        }
        NotificationUtil.createNotificationChannel(this)
    }
}