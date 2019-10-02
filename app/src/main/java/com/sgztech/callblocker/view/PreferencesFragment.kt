package com.sgztech.callblocker.view


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.sgztech.callblocker.R


class PreferencesFragment :
    PreferenceFragmentCompat(){

    private val preferenceBlockList by lazy {
        findPreference<SwitchPreferenceCompat>(getString(R.string.key_block_list))
    }
    private val preferenceAllList by lazy {
        findPreference<SwitchPreferenceCompat>(getString(R.string.key_block_all))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        preferenceAllList?.setOnPreferenceChangeListener { _, newValue ->
            val value = newValue as Boolean
            if(value){
                preferenceBlockList?.isChecked = false
            }
            true
        }

        preferenceBlockList?.setOnPreferenceChangeListener { _, newValue ->
            val value = newValue as Boolean
            if(value){
                preferenceAllList?.isChecked = false
            }
            true
        }

    }
}
