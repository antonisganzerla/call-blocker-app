package com.sgztech.callblocker.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sgztech.callblocker.R
import com.sgztech.callblocker.extension.toPtBrDateString
import com.sgztech.callblocker.extension.toTelephoneFormated
import com.sgztech.callblocker.util.PreferenceUtil.notify
import java.util.*

object NotificationUtil {

    private const val CHANNEL_ID = "CALL_BLOCKER_CHANNEL"

    @JvmStatic
    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @JvmStatic
    fun sendNotification(context: Context, phoneNumber: String) {
        if(notify(context)){
            val date = Date().toPtBrDateString()
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.phone_cancel)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_message, phoneNumber.toTelephoneFormated(), date))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            NotificationManagerCompat.from(context).notify(1, builder.build())
        }else{
            Log.w("NotificationUtil", context.getString(R.string.not_sent_notification_log))
        }
    }
}