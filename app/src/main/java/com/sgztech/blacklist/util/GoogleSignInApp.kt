package com.sgztech.blacklist.util

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GoogleSignInApp {
    @JvmStatic
    fun getGso(): GoogleSignInOptions{
        return  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    @JvmStatic
    fun getGoogleSignInClient(context: Context): GoogleSignInClient{
        return GoogleSignIn.getClient(context, getGso())
    }

}