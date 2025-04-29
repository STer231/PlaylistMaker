package com.practicum.playlistmaker.playlist.settings.domain

import com.practicum.playlistmaker.playlist.settings.domain.model.ThemeSettings

interface SettingsInteractor {
    fun loadTheme(): ThemeSettings
    fun updateTheme(isDark: Boolean)
}