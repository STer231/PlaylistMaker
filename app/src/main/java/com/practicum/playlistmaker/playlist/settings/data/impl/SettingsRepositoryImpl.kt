package com.practicum.playlistmaker.playlist.settings.data.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.playlist.settings.domain.SettingsRepository
import com.practicum.playlistmaker.playlist.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    companion object {
        private const val PREFERENCES_DARK_THEME = "dark_theme"
    }

    override fun getThemeSettings(): ThemeSettings {
        val isDark = sharedPreferences.getBoolean(PREFERENCES_DARK_THEME, false)
        return ThemeSettings(isDark)
    }

    override fun saveThemeSettings(settings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(PREFERENCES_DARK_THEME, settings.isDarkMode)
            .apply()
    }
}