package com.sgztech.blacklist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgztech.blacklist.dao.ContactDao
import com.sgztech.blacklist.model.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}