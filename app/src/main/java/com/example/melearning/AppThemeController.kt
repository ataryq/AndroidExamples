package com.example.melearning

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.melearning.fragments.main_activity.MainActivity

class AppThemeController(private val activity: MainActivity) :
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(activity)

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == getThemeKeyString()) {
            activity.recreate()
        }
    }

    fun getTheme(): Int {
        val isDarkTheme = preferences.getBoolean(getThemeKeyString(), true)
        return if(isDarkTheme) R.style.CustomAppThemeDark else R.style.CustomAppThemeLight
    }

    private fun getThemeKeyString() = activity.getString(R.string.theme_store_key)
}

