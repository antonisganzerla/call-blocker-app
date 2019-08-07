package com.sgztech.callblocker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgztech.callblocker.dao.ContactDao
import com.sgztech.callblocker.model.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}