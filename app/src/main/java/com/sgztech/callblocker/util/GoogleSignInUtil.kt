package com.sgztech.callblocker.util

import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sgztech.callblocker.R
import com.sgztech.callblocker.extension.showLog
import com.sgztech.callblocker.view.BaseActivity

object GoogleSignInUtil {

    @JvmStatic
    fun googleSignInOptions(): GoogleSignInOptions{
        return  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    @JvmStatic
    fun googleSignInClient(context: Context): GoogleSignInClient{
        return GoogleSignIn.getClient(context, googleSignInOptions())
    }

    @JvmStatic
    fun googleSignLogout(activity: Activity, callback: () -> Unit){
        googleSignInClient(activity).signOut()
            .addOnCompleteListener(activity) {
                callback()
            }
    }

    @JvmStatic
    fun signOut(activity: BaseActivity) {
        googleSignLogout(activity) {
            activity.showLog(activity.getString(R.string.msg_logout_success))
        }
    }

}