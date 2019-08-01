package com.sgztech.blacklist.extension

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.sgztech.blacklist.view.BaseActivity

fun BaseActivity.showLog(message: String) {
    Log.w(TAG_DEBUG, message)
}

fun BaseActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun BaseActivity.openActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
    finish()
}
