package com.sgztech.callblocker.extension

import android.content.Intent
import android.util.Log
import com.sgztech.callblocker.util.ToastUtil
import com.sgztech.callblocker.view.BaseActivity

fun BaseActivity.showLog(message: String) {
    Log.w(TAG_DEBUG, message)
}

fun BaseActivity.showToast(resourceMessage: Int) {
   ToastUtil.show(this, resourceMessage)
}


fun BaseActivity.openActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
    finish()
}
