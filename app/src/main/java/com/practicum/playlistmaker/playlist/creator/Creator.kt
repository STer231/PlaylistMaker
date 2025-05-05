package com.practicum.playlistmaker.playlist.creator

import android.content.Context
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.playlist.player.data.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.playlist.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.playlist.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.playlist.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.playlist.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.playlist.search.domain.repository.TrackRepository
import com.practicum.playlistmaker.playlist.search.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.playlist.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.playlist.search.domain.impl.SearchHistoryInteractor
import com.practicum.playlistmaker.playlist.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.playlist.search.domain.impl.SearchTracksInteractor
import com.practicum.playlistmaker.playlist.search.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.playlist.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.playlist.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.playlist.sharing.data.impl.SharingConfig
import com.practicum.playlistmaker.playlist.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.playlist.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.playlist.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.playlist.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideSearchTracksInteractor(context: Context): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTrackRepository(context))
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl()
    }

    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        val sharedPreferences =
            context.getSharedPreferences(SearchHistoryRepositoryImpl.PREFERENCES_HISTORY, Context.MODE_PRIVATE)
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository(context))
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