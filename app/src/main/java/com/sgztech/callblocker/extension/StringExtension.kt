package com.sgztech.callblocker.extension

import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.lang.Exception

fun String.toTelephoneFormated(): String {
    try {
        val phoneUtil = PhoneNumberUtil.getInstance()
        val phoneNumberFormated = phoneUtil.parse(this, "BR")
        return phoneUtil.format(phoneNumberFormated, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
    }catch (e: Exception){
        return this
    }
}

