package com.sgztech.blacklist.log

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.sgztech.blacklist.util.Constants.Companion.URL_LOADER
import java.util.*


class LooperApp(private val activity: AppCompatActivity) : LoaderManager.LoaderCallbacks<Cursor> {

    init {
        LoaderManager.getInstance(activity).initLoader(URL_LOADER, null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        Log.d(TAG, "onCreateLoader() >> loaderID : $id")
        when (id) {
            URL_LOADER -> return CursorLoader(
                activity,
                CallLog.Calls.CONTENT_URI, null, null, null, null
            )
            else -> throw Exception()
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        Log.d(TAG, "onLoadFinished()")
        val list = mutableListOf<CallLogApp>()
        data?.let {
            while (it.moveToNext()) {
                val phNumber = data.getString(data.getColumnIndex(CallLog.Calls.NUMBER))
                val callType = data.getString(data.getColumnIndex(CallLog.Calls.TYPE))
                val callDate = data.getString(data.getColumnIndex(CallLog.Calls.DATE))
                val callDayTime = Date(callDate.toLong())
                val callDuration = data.getString(data.getColumnIndex(CallLog.Calls.DURATION))
                val countryIso = data.getString(data.getColumnIndex(CallLog.Calls.COUNTRY_ISO))
                val isNew = data.getString(data.getColumnIndex(CallLog.Calls.NEW))
                val numberPresentation = data.getString(data.getColumnIndex(CallLog.Calls.NUMBER_PRESENTATION))

                val presentationOfNumber = when (numberPresentation.toInt()) {
                    CallLog.Calls.PRESENTATION_ALLOWED ->  "Allowed"
                    CallLog.Calls.PRESENTATION_RESTRICTED -> "Restricted"
                    CallLog.Calls.PRESENTATION_UNKNOWN -> "UnKnown"
                    CallLog.Calls.PRESENTATION_PAYPHONE -> "PayPhone"
                    else -> ""
                }

                val dir = when (callType.toInt()) {
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    CallLog.Calls.VOICEMAIL_TYPE -> "Voice Mail"
                    else -> ""
                }
                val callLogApp = CallLogApp(
                    phNumber,
                    callType,
                    callDate,
                    callDuration,
                    dir,
                    callDayTime,
                    countryIso,
                    isNew,
                    presentationOfNumber
                    )
                list.add(callLogApp)
            }
        }
        Log.i(TAG, "${list.toList()}")
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        Log.d(TAG, "onLoaderReset()")
    }


    companion object{
        const val TAG = "CallLogApp"
    }
}