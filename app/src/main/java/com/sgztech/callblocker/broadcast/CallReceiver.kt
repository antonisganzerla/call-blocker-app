package com.sgztech.callblocker.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import com.android.internal.telephony.ITelephony
import com.sgztech.callblocker.R
import com.sgztech.callblocker.core.CoreApplication
import com.sgztech.callblocker.util.Constants
import com.sgztech.callblocker.util.NotificationUtil.sendNotification
import com.sgztech.callblocker.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.w(TAG_DEBUG, context.getString(R.string.msg_call_broadcast))
        intent.extras?.let { bundle ->
            val state = bundle.getString(TelephonyManager.EXTRA_STATE)
            val phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            Log.w(TAG_DEBUG, context.getString(R.string.msg_phone_state, state))

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                phoneNumber?.let {
                    checkRulesToBlockCall(context, phoneNumber)
                }
            }
        }
    }

    private fun endCallBelowPieVersion(context: Context, phoneNumber: String) {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            val m = tm.javaClass.getDeclaredMethod("getITelephony")
            m.isAccessible = true
            val telephonyService = m.invoke(tm) as ITelephony
            val result = telephonyService.endCall()
            showLogAfterCall(result, phoneNumber, context)
            sendNotification(context, phoneNumber)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun endCallAbovePieVersion(context: Context, phoneNumber: String) {
        val tm = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val result = tm.endCall()
        showLogAfterCall(result, phoneNumber, context)
        sendNotification(context, phoneNumber)
    }

    private fun checkRulesToBlockCall(context: Context, phoneNumber: String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            if (PreferenceUtil.blockAllCall(context)) {
                endCall(context, phoneNumber)
            }

            if (PreferenceUtil.blockOnlyList(context) && numberPhoneIsBlockList(phoneNumber)) {
                endCall(context, phoneNumber)
            }
        }
    }

    private fun endCall(context: Context, phoneNumber: String) {
        if (Constants.CURRENT_VERSION_CODE >= Constants.VERSION_CODE_PIE) {
            endCallAbovePieVersion(context, phoneNumber)
        } else {
            endCallBelowPieVersion(context, phoneNumber)
        }
    }

    private suspend fun numberPhoneIsBlockList(phoneNumber: String): Boolean {
        val result = GlobalScope.async {
            val dao = CoreApplication.database?.contactDao()
            dao?.load(phoneNumber)
        }
        return result.await()!! > 0
    }

    private fun showLogAfterCall(result: Boolean, phoneNumber: String?, context: Context) {
        if (result) {
            showLogSuccessEndCall(context, phoneNumber)
        } else {
            showLogFailEndCall(context)
        }
    }

    private fun showLogFailEndCall(context: Context) {
        Log.w(TAG_DEBUG, context.getString(R.string.msg_fail_end_call))
    }

    private fun showLogSuccessEndCall(context: Context, phoneNumber: String?) {
        phoneNumber?.let {
            Log.w(TAG_DEBUG, context.getString(R.string.msg_sucess_end_call, it))
        }
    }

    companion object {
        val TAG_DEBUG = CallReceiver.javaClass.name
    }
}

