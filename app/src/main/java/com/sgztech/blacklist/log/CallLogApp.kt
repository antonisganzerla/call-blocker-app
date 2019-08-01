package com.sgztech.blacklist.log

import java.util.*

data class CallLogApp(
    private var number: String?,
    private val type: String?,
    private val date: String?,
    private val duration: String?,
    private val direction: String?,
    private val callDayTime: Date?,
    private val countryIso: String?,
    private val isNew: String?,
    private val presentationOfNumber: String?
)