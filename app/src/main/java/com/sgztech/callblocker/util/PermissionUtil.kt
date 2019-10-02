package com.sgztech.callblocker.util

import android.Manifest
import com.sgztech.callblocker.R
import com.sgztech.callblocker.extension.showLog
import com.sgztech.callblocker.view.BaseActivity
import com.sgztech.callblocker.view.MainActivity

object PermissionUtil {

    @JvmStatic
    fun havePermissions(activity: BaseActivity): Boolean{
        if (Constants.CURRENT_VERSION_CODE >= Constants.VERSION_CODE_PIE) {
            val list = necessaryPermissions().plus(Manifest.permission.ANSWER_PHONE_CALLS)
            return checkPermission(
                activity,
                Constants.CURRENT_VERSION_CODE,
                list,
                MainActivity.PERMISSION_REQUEST_PHONE_CALL
            )
        } else if (Constants.CURRENT_VERSION_CODE >= Constants.VERSION_CODE_MARSHMALLOW) {
            return checkPermission(
                activity,
                Constants.CURRENT_VERSION_CODE,
                necessaryPermissions(),
                MainActivity.PERMISSION_REQUEST_PHONE_CALL
            )
        } else {
            return true
        }
    }

    private fun necessaryPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )
    }

    private fun checkPermission(activity: BaseActivity, minVersionCode: Int, permissions: Array<String>, requestCode: Int): Boolean {

        var havePermission = false
        if (minVersionCode < Constants.VERSION_CODE_MARSHMALLOW || permissions.isEmpty()) {
            return false
        }

        if (Constants.CURRENT_VERSION_CODE >= minVersionCode) {
            try {
                if (activity.checkSelfPermission(permissions[0]) == Constants.PERMISSION_DENIED) {
                    activity.requestPermissions(permissions, requestCode)
                } else {
                    havePermission = true
                }
            } catch (e: Exception) {
                e.message?.let {
                    activity.showLog(it)
                }
            }
        } else {
            activity.showLog(activity.getString(R.string.msg_unnecessary_check_permission))
        }
        return havePermission
    }

    @JvmStatic
    fun checkResultPermission(activity: BaseActivity, grantResults: IntArray, permissions: Array<out String>): Boolean {
        if (grantResults.isNotEmpty() && grantResults[0] == Constants.PERMISSION_GRANTED) {
            activity.showLog(activity.getString(R.string.msg_permission_granted, permissions.asList()))
            return true
        } else {
            activity.showLog(activity.getString(R.string.msg_permission_not_granted_list, permissions.asList()))
            return false
        }
    }


}