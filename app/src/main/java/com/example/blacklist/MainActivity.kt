package com.example.blacklist

import android.Manifest.permission.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.blacklist.Constants.Companion.CURRENT_VERSION_CODE
import com.example.blacklist.Constants.Companion.PERMISSION_DENIED
import com.example.blacklist.Constants.Companion.PERMISSION_GRANTED
import com.example.blacklist.Constants.Companion.VERSION_CODE_MARSHMALLOW
import com.example.blacklist.Constants.Companion.VERSION_CODE_PIE


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (CURRENT_VERSION_CODE >= VERSION_CODE_PIE) {
            checkPermission(
                CURRENT_VERSION_CODE,
                arrayOf(READ_PHONE_STATE, CALL_PHONE, ANSWER_PHONE_CALLS),
                PERMISSION_REQUEST_PHONE_CALL
            )
        } else if (CURRENT_VERSION_CODE >= VERSION_CODE_MARSHMALLOW) {
            checkPermission(
                CURRENT_VERSION_CODE,
                arrayOf(READ_PHONE_STATE, CALL_PHONE),
                PERMISSION_REQUEST_PHONE_CALL
            )
        }
    }

    private fun checkPermission(minVersionCode: Int, permissions: Array<String>, requestCode: Int) {
        if (minVersionCode < VERSION_CODE_MARSHMALLOW || permissions.isEmpty()) {
            Log.w(TAG_DEBUG, getString(R.string.msg_permissions_not_checked))
            return
        }

        if (CURRENT_VERSION_CODE >= minVersionCode) {
            try {
                if (checkSelfPermission(permissions[0]) == PERMISSION_DENIED) {
                    requestPermissions(permissions, requestCode)
                }
            } catch (e: Exception) {
                Log.w(TAG_DEBUG, e)
            }
        }else{
            Log.w(TAG_DEBUG, getString(R.string.msg_unnecessary_check_permission))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_PHONE_CALL -> {
                checkResultPermission(grantResults, permissions)
                return
            }
        }
    }

    private fun checkResultPermission(grantResults: IntArray, permissions: Array<out String>) {
        if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            Log.w(TAG_DEBUG, getString(R.string.msg_permission_granted, permissions.asList()))
        } else {
            Toast.makeText(
                this,
                getString(R.string.msg_permission_not_granted),
                Toast.LENGTH_SHORT
            ).show()
            Log.w(TAG_DEBUG, getString(R.string.msg_permission_not_granted_list, permissions.asList()))
        }
    }

    companion object {
        const val PERMISSION_REQUEST_PHONE_CALL = 1
        val TAG_DEBUG = MainActivity::javaClass.name
    }
}
