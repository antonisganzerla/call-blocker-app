package com.sgztech.blacklist.loader

import java.util.*

data class CallLogApp(
    val number: String?,
    val type: String?,
    val date: String?,
    val duration: String?,
    val direction: String?,
    val callDayTime: Date?,
    val countryIso: String?,
    val isNew: String?,
    val presentationOfNumber: String?
)