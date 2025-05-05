package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun saveThemeSettings(settings: ThemeSettings)
}