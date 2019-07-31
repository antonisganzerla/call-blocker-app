package com.example.blacklist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import com.android.internal.telephony.ITelephony
import com.example.blacklist.Constants.Companion.CURRENT_VERSION_CODE
import com.example.blacklist.Constants.Companion.VERSION_CODE_PIE

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.w(TAG_DEBUG, context.getString(R.string.msg_call_broadcast))
        intent.extras?.let { bundle ->
            val state = bundle.getString(TelephonyManager.EXTRA_STATE)
            val phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)

            Log.w(TAG_DEBUG, context.getString(R.string.msg_phone_state, state))
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {

                if (CURRENT_VERSION_CODE >= VERSION_CODE_PIE) {
                    val tm = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                    val result = tm.endCall()
                    showLogAfterCall(result, phoneNumber, context)
                } else {
                    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    try {
                        val m = tm.javaClass.getDeclaredMethod("getITelephony")
                        m.isAccessible = true
                        val telephonyService = m.invoke(tm) as ITelephony
                        val result = telephonyService.endCall()
                        showLogAfterCall(result, phoneNumber, context)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun showLogAfterCall(result: Boolean, phoneNumber: String?, context: Context){
        if(result){
            showLogSuccessEndCall(context, phoneNumber)
        }else{
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
        val TAG_DEBUG = CallReceiver::javaClass.name
    }
}
