package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.player.data.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.TrackRepository
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.impl.SharingConfig
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl()
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val sharedPreferences =
            context.getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val settingsRepository = SettingsRepositoryImpl(sharedPreferences)
        return SettingsInteractorImpl(settingsRepository)
    }

    private fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context.applicationContext)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        val config = SharingConfig(context.applicationContext)
        return SharingInteractorImpl(provideExternalNavigator(context), config)
    }
}