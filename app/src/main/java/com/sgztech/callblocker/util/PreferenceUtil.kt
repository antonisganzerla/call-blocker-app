package com.sgztech.callblocker.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.sgztech.callblocker.R

object PreferenceUtil{


    @JvmStatic
    fun blockOnlyList(context: Context): Boolean{
        return  booleanPreference(
            context,
            R.string.key_block_list,
            R.bool.block_list_setting_default
        )
    }

    @JvmStatic
    fun blockAllCall(context: Context): Boolean{
        return  booleanPreference(
            context,
            R.string.key_block_all,
            R.bool.block_all_setting_default
        )
    }

    @JvmStatic
    fun notify(context: Context): Boolean{
        return  booleanPreference(
            context,
            R.string.key_notification,
            R.bool.notification_setting_default
        )
    }

    private fun booleanPreference(context: Context, resourceKey: Int, resourceDefaultValue: Int): Boolean{
        return sharedPreferences(context).getBoolean(
            context.resources.getString(resourceKey),
            context.resources.getBoolean(resourceDefaultValue)
        )
    }

    @JvmStatic
    fun sharedPreferences(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
}