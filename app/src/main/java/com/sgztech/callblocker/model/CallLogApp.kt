package com.sgztech.callblocker.model

import java.util.*

data class CallLogApp(
    val contact: Contact,
    val type: String?,
    val date: String?,
    val duration: String?,
    val direction: String?,
    val callDayTime: Date?,
    val countryIso: String?,
    val isNew: String?,
    val presentationOfNumber: String?
)