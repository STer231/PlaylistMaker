package com.practicum.playlistmaker.playlist.settings.domain

import com.practicum.playlistmaker.playlist.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun saveThemeSettings(settings: ThemeSettings)
}