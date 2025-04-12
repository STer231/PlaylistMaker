package com.practicum.playlistmaker.domain.repository

interface SettingsRepository {
    fun isDarkTheme(): Boolean
    fun switchTheme(isDark: Boolean)
}