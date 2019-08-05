package com.sgztech.blacklist.dao

import androidx.room.*
import com.sgztech.blacklist.model.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM CONTACT")
    fun all(): List<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg contact: Contact)

    @Update
    fun update(contact: Contact)

    @Delete
    fun delete(contact: Contact)
}