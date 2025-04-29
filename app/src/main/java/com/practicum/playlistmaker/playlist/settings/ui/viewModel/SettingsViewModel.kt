package com.practicum.playlistmaker.playlist.settings.ui.viewModel

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.playlist.creator.Creator
import com.practicum.playlistmaker.playlist.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.playlist.sharing.domain.SharingInteractor

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