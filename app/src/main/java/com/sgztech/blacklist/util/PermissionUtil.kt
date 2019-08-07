package com.sgztech.blacklist.util

import android.Manifest
import com.sgztech.blacklist.R
import com.sgztech.blacklist.extension.showLog
import com.sgztech.blacklist.view.BaseActivity
import com.sgztech.blacklist.view.MainActivity

object PermissionUtil {

    @JvmStatic
    fun havePermissions(activity: BaseActivity): Boolean{
        val listPermissions = necessaryPermissions()
        if (Constants.CURRENT_VERSION_CODE >= Constants.VERSION_CODE_PIE) {
            listPermissions.plus(Manifest.permission.ANSWER_PHONE_CALLS)
            return checkPermission(
                activity,
                Constants.CURRENT_VERSION_CODE,
                listPermissions,
                MainActivity.PERMISSION_REQUEST_PHONE_CALL
            )
        } else if (Constants.CURRENT_VERSION_CODE >= Constants.VERSION_CODE_MARSHMALLOW) {
            return checkPermission(
                activity,
                Constants.CURRENT_VERSION_CODE,
                listPermissions,
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
            Manifest.permission.WRITE_CALL_LOG
        )
    }

    private fun checkPermission(activity: BaseActivity, minVersionCode: Int, permissions: Array<String>, requestCode: Int): Boolean {

        var havePermission = false
        if (minVersionCode < Constants.VERSION_CODE_MARSHMALLOW || permissions.isEmpty()) {
            //showLog(getString(R.string.msg_permissions_not_checked))
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
}