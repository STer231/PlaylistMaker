package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsInteractor {
    fun loadTheme(): ThemeSettings
    fun updateTheme(isDark: Boolean)
}