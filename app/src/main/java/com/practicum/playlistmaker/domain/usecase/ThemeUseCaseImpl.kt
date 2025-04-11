package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.repository.SettingsRepository

class ThemeUseCaseImpl(private val settingsRepository: SettingsRepository) : ThemeUseCase {
    override fun getCurrentTheme(): Boolean {
        return settingsRepository.isDarkTheme()
    }

    override fun updateTheme(isDark: Boolean) {
        settingsRepository.switchTheme(isDark)
    }
}