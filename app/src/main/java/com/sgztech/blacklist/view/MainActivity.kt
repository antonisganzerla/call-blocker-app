package com.sgztech.blacklist.view

import android.Manifest.permission.*
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.sgztech.blacklist.R
import com.sgztech.blacklist.extension.openActivity
import com.sgztech.blacklist.extension.showLog
import com.sgztech.blacklist.extension.showToast
import com.sgztech.blacklist.util.Constants.Companion.CURRENT_VERSION_CODE
import com.sgztech.blacklist.util.Constants.Companion.PERMISSION_DENIED
import com.sgztech.blacklist.util.Constants.Companion.PERMISSION_GRANTED
import com.sgztech.blacklist.util.Constants.Companion.VERSION_CODE_MARSHMALLOW
import com.sgztech.blacklist.util.Constants.Companion.VERSION_CODE_PIE
import com.sgztech.blacklist.util.GoogleSignInApp.getGoogleSignInClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : BaseActivity() {

    private val account: GoogleSignInAccount? by lazy {
        GoogleSignIn.getLastSignedInAccount(this)
    }
    private var fragmentPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            fragmentPosition = it.getInt(SAVED_INSTANCE_CURRENT_FRAGMENT_KEY)
        }

        setupToolbar()
        setupDrawer()
        setupPermissions()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putInt(SAVED_INSTANCE_CURRENT_FRAGMENT_KEY, fragmentPosition)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupDrawerItemClickListener()
        setupHeaderDrawer()
    }

    private fun setupPermissions() {
        if (havePermissions()) {
            openCallLogFragment()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupHeaderDrawer() {
        val headerView = navView.getHeaderView(0)
        headerView?.let {
            it.nav_header_name.text = account?.displayName
            it.nav_header_email.text = account?.email
//            Picasso.get().setIndicatorsEnabled(true)
//            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(account?.photoUrl).into(it.nav_header_imageView)
        }
    }

    private fun setupDrawerItemClickListener() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_item_one -> {
                    openCallLogFragment()
                }
                R.id.nav_item_two -> {
                    displayView(1, "Black List")
                }
                R.id.nav_item_seven -> {
                    signOut()
                    openActivity(LoginActivity::class.java)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun openCallLogFragment(){
        displayView(INIT_POSITION_FRAGMENT, "Logs")
    }


    private fun havePermissions(): Boolean {
        val listPermissions = arrayOf(READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG)
        if (CURRENT_VERSION_CODE >= VERSION_CODE_PIE) {
            listPermissions.plus(ANSWER_PHONE_CALLS)
            return checkPermission(
                CURRENT_VERSION_CODE,
                listPermissions,
                PERMISSION_REQUEST_PHONE_CALL
            )
        } else if (CURRENT_VERSION_CODE >= VERSION_CODE_MARSHMALLOW) {
            return checkPermission(
                CURRENT_VERSION_CODE,
                listPermissions,
                PERMISSION_REQUEST_PHONE_CALL
            )
        } else {
            return true
        }
    }

    private fun checkPermission(minVersionCode: Int, permissions: Array<String>, requestCode: Int): Boolean {

        var havePermission = false
        if (minVersionCode < VERSION_CODE_MARSHMALLOW || permissions.isEmpty()) {
            showLog(getString(R.string.msg_permissions_not_checked))
            return false
        }

        if (CURRENT_VERSION_CODE >= minVersionCode) {
            try {
                if (checkSelfPermission(permissions[0]) == PERMISSION_DENIED) {
                    requestPermissions(permissions, requestCode)
                } else {
                    havePermission = true
                }
            } catch (e: Exception) {
                e.message?.let {
                    showLog(it)
                }
            }
        } else {
            showLog(getString(R.string.msg_unnecessary_check_permission))
        }
        return havePermission
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_PHONE_CALL -> {
                if (checkResultPermission(grantResults, permissions)) {
                    openCallLogFragment()
                }
                return
            }
        }
    }

    private fun displayView(position: Int, title: String) {

        if(position != fragmentPosition){
            val fragment = when (position) {
                0 -> CallLogFragment()
                1 -> BlackListFragment()
                else -> {
                    CallLogFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment, null).commitAllowingStateLoss()
            toolbar.title = title
            fragmentPosition = position
        }
    }

    private fun checkResultPermission(grantResults: IntArray, permissions: Array<out String>): Boolean {
        if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            showLog(getString(R.string.msg_permission_granted, permissions.asList()))
            return true
        } else {
            showToast(getString(R.string.msg_permission_not_granted))
            showLog(getString(R.string.msg_permission_not_granted_list, permissions.asList()))
            return false
        }
    }

    private fun signOut() {

        getGoogleSignInClient(this)
            .signOut()
            .addOnCompleteListener(this) {
                showLog(getString(R.string.msg_logout_success))
            }
    }

    override val TAG_DEBUG: String
        get() = MainActivity.javaClass.name

    companion object {
        const val PERMISSION_REQUEST_PHONE_CALL = 1
        const val INIT_POSITION_FRAGMENT = 0
        const val SAVED_INSTANCE_CURRENT_FRAGMENT_KEY = "SAVED_INSTANCE_CURRENT_FRAGMENT_KEY"
    }
}
