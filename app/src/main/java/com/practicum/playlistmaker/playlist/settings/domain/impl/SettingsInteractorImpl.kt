package com.practicum.playlistmaker.playlist.settings.domain.impl

import com.practicum.playlistmaker.playlist.settings.domain.SettingsRepository
import com.practicum.playlistmaker.playlist.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.playlist.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun loadTheme(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateTheme(isDark: Boolean) {
        settingsRepository.saveThemeSettings(ThemeSettings(isDark))
    }
}