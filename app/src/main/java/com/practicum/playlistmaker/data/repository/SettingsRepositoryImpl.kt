package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    companion object {
        private const val PREFERENCES_DARK_THEME = "dark_theme"
    }

    override fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(PREFERENCES_DARK_THEME, false)
    }

    override fun switchTheme(isDark: Boolean) {
        sharedPreferences.edit()
            .putBoolean(PREFERENCES_DARK_THEME, isDark)
            .apply()
    }
}