package com.example.melearning.fragments

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceFragmentCompat
import com.example.melearning.fragments.main_activity.MainActivityViewModel
import com.example.melearning.R

class SettingsFragment: PreferenceFragmentCompat() {
    private val mActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey)
    }
}