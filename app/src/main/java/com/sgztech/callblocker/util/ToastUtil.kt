package com.sgztech.callblocker.util

import android.content.Context
import android.widget.Toast

object ToastUtil {

    @JvmStatic
    fun show(context: Context, resourceMessage: Int){
        Toast.makeText(context, context.getString(resourceMessage), Toast.LENGTH_SHORT).show()
    }

}