package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.SettingsUiState
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    companion object {
        fun getViewModelFactory(
            settingsInteractor: SettingsInteractor,
            sharingInteractor: SharingInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(settingsInteractor, sharingInteractor)
            }
        }
    }

    private val stateSettingsLiveData = MutableLiveData<SettingsUiState>()
    fun observeState(): LiveData<SettingsUiState> = stateSettingsLiveData

    init {
        val current = settingsInteractor.loadTheme()
        stateSettingsLiveData.value = SettingsUiState(current.isDarkMode)
    }

    fun toggleTheme(isDark: Boolean) {
        settingsInteractor.updateTheme(isDark)
        stateSettingsLiveData.value = SettingsUiState(isDark)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openUserAgreement() {
        sharingInteractor.openTerms()
    }

    fun contactSupport() {
        sharingInteractor.openSupport()
    }
}