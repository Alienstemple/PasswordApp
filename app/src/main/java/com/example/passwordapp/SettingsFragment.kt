package com.example.passwordapp

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (getString(R.string.app_name/* имя нашего pref */) == preference.key) {
            // действие
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}