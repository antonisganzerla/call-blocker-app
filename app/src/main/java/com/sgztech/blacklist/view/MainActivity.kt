package com.sgztech.blacklist.view

import android.Manifest.permission.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sgztech.blacklist.R
import com.sgztech.blacklist.log.LooperApp
import com.sgztech.blacklist.util.Constants.Companion.CURRENT_VERSION_CODE
import com.sgztech.blacklist.util.Constants.Companion.PERMISSION_DENIED
import com.sgztech.blacklist.util.Constants.Companion.PERMISSION_GRANTED
import com.sgztech.blacklist.util.Constants.Companion.VERSION_CODE_MARSHMALLOW
import com.sgztech.blacklist.util.Constants.Companion.VERSION_CODE_PIE
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {

    private val account: GoogleSignInAccount? by lazy {
        GoogleSignIn.getLastSignedInAccount(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setupDrawer()
        if(havePermissions()){
            setupLoader()
        }
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_item_one -> {
                    Toast.makeText(this, "Menu 1", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_item_two -> {
                    Toast.makeText(this, "Menu 3", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_item_seven -> {
                    signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val headerView = navView.getHeaderView(0)
        headerView?.let {
            it.nav_header_name.text = account?.displayName
            it.nav_header_email.text = account?.email
            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(it.nav_header_imageView)
            Picasso.get().load(account?.photoUrl).into(it.nav_header_imageView)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun havePermissions(): Boolean{
        if (CURRENT_VERSION_CODE >= VERSION_CODE_PIE) {
            return checkPermission(
                CURRENT_VERSION_CODE,
                arrayOf(READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG, ANSWER_PHONE_CALLS),
                PERMISSION_REQUEST_PHONE_CALL
            )
        } else if (CURRENT_VERSION_CODE >= VERSION_CODE_MARSHMALLOW) {
            return checkPermission(
                CURRENT_VERSION_CODE,
                arrayOf(READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG),
                PERMISSION_REQUEST_PHONE_CALL
            )
        }else{
            return true
        }
    }

    private fun checkPermission(minVersionCode: Int, permissions: Array<String>, requestCode: Int): Boolean {

        var havePermission = false
        if (minVersionCode < VERSION_CODE_MARSHMALLOW || permissions.isEmpty()) {
            Log.w(TAG_DEBUG, getString(R.string.msg_permissions_not_checked))
            return false
        }

        if (CURRENT_VERSION_CODE >= minVersionCode) {
            try {
                if (checkSelfPermission(permissions[0]) == PERMISSION_DENIED) {
                    requestPermissions(permissions, requestCode)
                }else{
                    havePermission = true
                }
            } catch (e: Exception) {
                Log.w(TAG_DEBUG, e)
            }
        }else{
            Log.w(TAG_DEBUG, getString(R.string.msg_unnecessary_check_permission))
        }
        return havePermission
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_PHONE_CALL -> {
                if(checkResultPermission(grantResults, permissions)){
                    setupLoader()
                }
                return
            }
        }
    }

    private fun setupLoader() {
        LooperApp(this)
    }

    private fun checkResultPermission(grantResults: IntArray, permissions: Array<out String>): Boolean {
        if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            Log.w(TAG_DEBUG, getString(R.string.msg_permission_granted, permissions.asList()))
            return true
        } else {
            Toast.makeText(
                this,
                getString(R.string.msg_permission_not_granted),
                Toast.LENGTH_SHORT
            ).show()
            Log.w(TAG_DEBUG, getString(R.string.msg_permission_not_granted_list, permissions.asList()))
            return false
        }
    }

    private fun signOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                Log.w(TAG_DEBUG, "Logout with success")
            }
    }

    companion object {
        const val PERMISSION_REQUEST_PHONE_CALL = 1
        val TAG_DEBUG = MainActivity::javaClass.name
    }
}
