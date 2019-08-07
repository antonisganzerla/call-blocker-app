package com.sgztech.callblocker.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.sgztech.callblocker.R
import com.sgztech.callblocker.extension.openActivity
import com.sgztech.callblocker.util.AlertDialogUtil
import com.sgztech.callblocker.util.GoogleSignInUtil.signOut
import com.sgztech.callblocker.util.PermissionUtil.checkResultPermission
import com.sgztech.callblocker.util.PermissionUtil.havePermissions
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
            fragmentPosition = it.getInt(CURRENT_FRAGMENT_KEY)
        }

        setupToolbar()
        setupDrawer()
        openCallLogFragment()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putInt(CURRENT_FRAGMENT_KEY, fragmentPosition)
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
                R.id.nav_item_call -> {
                    openCallLogFragment()
                }
                R.id.nav_item_block -> {
                    displayView(1, getString(R.string.title_block_list_fragment))
                }
                R.id.nav_item_tools -> {
                    displayView(2, getString(R.string.title_preferences_fragment))
                }
                R.id.nav_item_logout -> {
                    showDialogLogout()
                }
                R.id.nav_item_about -> {
                    AlertDialogUtil.showSimpleDialog(
                        this,
                        R.string.dialog_about_app_title,
                        R.string.dialog_about_app_message
                    )
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun openCallLogFragment() {
        displayView(INIT_POSITION_FRAGMENT, getString(R.string.title_call_log_fragment))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_PHONE_CALL -> {
                if (checkResultPermission(this, grantResults, permissions)) {
                    openCallLogFragment()
                } else {
                    showDialogRefusedPermissions()
                }
                return
            }
        }
    }

    private fun displayView(position: Int, title: String) {
        if (havePermissions(this)) {
            if (position != fragmentPosition) {
                val fragment = when (position) {
                    0 -> CallLogFragment()
                    1 -> BlockListFragment()
                    2 -> PreferencesFragment()
                    else -> {
                        CallLogFragment()
                    }
                }
                supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment, null)
                    .commitAllowingStateLoss()
                fragmentPosition = position
            }
            toolbar.title = title
        }
    }

    private fun showDialogLogout() {
        val dialog = AlertDialogUtil.create(
            this,
            R.string.dialog_message_logout
        ) {
            logout()
        }
        dialog.show()
    }

    private fun logout() {
        signOut(this)
        openActivity(LoginActivity::class.java)
    }

    private fun showDialogRefusedPermissions() {
        val dialog = AlertDialogUtil.create(
            this,
            R.string.dialog_message_request_permission,
            {
                havePermissions(this)
            },
            {
                logout()
            }
        )
        dialog.setCancelable(false)
        dialog.show()
    }

    override val TAG_DEBUG: String
        get() = MainActivity.javaClass.name

    companion object {
        const val PERMISSION_REQUEST_PHONE_CALL = 1
        const val INIT_POSITION_FRAGMENT = 0
        const val CURRENT_FRAGMENT_KEY = "CURRENT_FRAGMENT_KEY"
    }
}
