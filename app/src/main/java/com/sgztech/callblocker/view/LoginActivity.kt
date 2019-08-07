package com.sgztech.callblocker.view

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sgztech.callblocker.R
import com.sgztech.callblocker.extension.openActivity
import com.sgztech.callblocker.extension.showLog
import com.sgztech.callblocker.util.GoogleSignInUtil.googleSignInClient
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {

    private val account: GoogleSignInAccount? by lazy {
        GoogleSignIn.getLastSignedInAccount(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sign_in_button.setOnClickListener {
            signIn()
        }

        account?.let {
            openMainActivity()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient(this).signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            showLog(getString(R.string.msg_signin_success, account?.displayName))
            openMainActivity()
        } catch (e: ApiException) {
            showLog(getString(R.string.msg_signin_fail, e.statusCode.toString()))
            showLog(getString(R.string.msg_signin_fail, e))
            // verificar coenxão com a internet;
        }

    }

    private fun openMainActivity() {
        openActivity(MainActivity::class.java)
    }

    override val TAG_DEBUG: String
        get() = MainActivity.javaClass.name

    companion object{
        const val RC_SIGN_IN = 1
    }
}
