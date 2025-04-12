package com.practicum.playlistmaker.domain.usecase

interface ThemeUseCase {
    fun getCurrentTheme(): Boolean
    fun updateTheme(isDark: Boolean)
}