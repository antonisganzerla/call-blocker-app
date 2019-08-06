package com.sgztech.blacklist.view


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.sgztech.blacklist.R


class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}

