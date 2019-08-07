package com.sgztech.callblocker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["number_phone"], unique = true)])
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "number_phone")
    val numberPhone: String,
    var blocked: Boolean = false,
    var blockedDate: String = "")