package com.sgztech.blacklist.loader

import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.sgztech.blacklist.R
import com.sgztech.blacklist.exception.CursorLoaderNotFoundException
import com.sgztech.blacklist.util.Constants.Companion.ID_LOG_CALL_LOADER
import java.util.*


class LogCallLoader(
    private val activity: AppCompatActivity,
    private val callback: (list: MutableList<CallLogApp>) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    init {
        LoaderManager.getInstance(activity).initLoader(ID_LOG_CALL_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        Log.d(TAG_DEBUG, activity.getString(R.string.msg_on_create_loader, id.toString()))
        when (id) {
            ID_LOG_CALL_LOADER -> return CursorLoader(
                activity,
                CallLog.Calls.CONTENT_URI, null, null, null, null
            )
            else -> throw CursorLoaderNotFoundException(id)
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        Log.d(TAG_DEBUG, activity.getString(R.string.msg_on_load_finished))
        val logCallList = mutableListOf<CallLogApp>()
        data?.let { cursor ->

            while (cursor.moveToNext()) {
                val callLogApp = buildLogCallApp(cursor)
                logCallList.add(callLogApp)
            }
        }
        Log.i(TAG_DEBUG, logCallList.toList().toString())
        callback(logCallList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        Log.d(TAG_DEBUG, activity.getString(R.string.msg_on_loader_reset))
    }


    private fun buildLogCallApp(cursor: Cursor): CallLogApp {
        val phNumber = getString(cursor, CallLog.Calls.NUMBER)
        val callType = getString(cursor, CallLog.Calls.TYPE)
        val callDate = getString(cursor, CallLog.Calls.DATE)
        val callDayTime = Date(callDate.toLong())
        val callDuration = getString(cursor, CallLog.Calls.DURATION)
        val countryIso = getString(cursor, CallLog.Calls.COUNTRY_ISO)
        val isNew = getString(cursor, CallLog.Calls.NEW)
        val numberPresentation = getString(cursor, CallLog.Calls.NUMBER_PRESENTATION)

        val presentationOfNumber = presentationNumberToString(numberPresentation)
        val direction = callTypeToString(callType)

        return CallLogApp(
            phNumber,
            callType,
            callDate,
            callDuration,
            direction,
            callDayTime,
            countryIso,
            isNew,
            presentationOfNumber
        )
    }

    private fun callTypeToString(callType: String): String {
        return when (callType.toInt()) {
            CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
            CallLog.Calls.INCOMING_TYPE -> "Incoming"
            CallLog.Calls.MISSED_TYPE -> "Missed"
            CallLog.Calls.VOICEMAIL_TYPE -> "Voice Mail"
            else -> ""
        }
    }

    private fun presentationNumberToString(numberPresentation: String): String {
        return when (numberPresentation.toInt()) {
            CallLog.Calls.PRESENTATION_ALLOWED -> "Allowed"
            CallLog.Calls.PRESENTATION_RESTRICTED -> "Restricted"
            CallLog.Calls.PRESENTATION_UNKNOWN -> "UnKnown"
            CallLog.Calls.PRESENTATION_PAYPHONE -> "PayPhone"
            else -> ""
        }
    }

    private fun getString(cursor: Cursor, collumn: String): String {
        return cursor.getString(cursor.getColumnIndex(collumn))
    }
    companion object {
        val TAG_DEBUG: String = LogCallLoader.javaClass.name
    }
}