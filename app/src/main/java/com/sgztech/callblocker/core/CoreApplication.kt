package com.sgztech.callblocker.core

import android.app.Application
import androidx.room.Room
import com.sgztech.callblocker.database.AppDatabase
import com.sgztech.callblocker.util.NotificationUtil

open class CoreApplication: Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "my-db").build()
        NotificationUtil.createNotificationChannel(this)
    }
}