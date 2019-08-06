package com.sgztech.blacklist.extension

import android.content.Intent
import android.util.Log
import com.sgztech.blacklist.util.ToastUtil
import com.sgztech.blacklist.view.BaseActivity

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
