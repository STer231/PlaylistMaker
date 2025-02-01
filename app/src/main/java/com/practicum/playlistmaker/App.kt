package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object PreferencesKeys {
        const val PREFERENCES_NAME = "theme_preferences"
        const val PREFERENCES_DARK_THEME = "dark_theme"
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        val darkThemeEnabled = sharedPreferences.getBoolean(PREFERENCES_DARK_THEME, false)

        applyTheme(darkThemeEnabled)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        applyTheme(darkThemeEnabled)
        seveThemePreferences(darkThemeEnabled)
    }

    private fun applyTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun seveThemePreferences(isDark: Boolean) {
        sharedPreferences.edit()
            .putBoolean(PREFERENCES_DARK_THEME, isDark)
            .apply()
    }

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(PREFERENCES_DARK_THEME, false)
    }
}