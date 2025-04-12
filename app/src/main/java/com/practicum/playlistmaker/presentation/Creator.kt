package com.practicum.playlistmaker.presentation

import android.content.Context
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.AudioPlayerStateRepositoryImpl
import com.practicum.playlistmaker.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.usecase.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.domain.repository.AudioPlayerStateRepository
import com.practicum.playlistmaker.domain.usecase.SearchTracksInteractor
import com.practicum.playlistmaker.domain.usecase.SearchTracksInteractorImpl
import com.practicum.playlistmaker.domain.usecase.ThemeUseCase
import com.practicum.playlistmaker.domain.usecase.ThemeUseCaseImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTrackRepository())
    }

    fun provideThemeUseCase(context: Context): ThemeUseCase {
        val sharedPreferences =
            context.getSharedPreferences("theme_preferences", Context.MODE_PRIVATE)
        val settingsRepository = SettingsRepositoryImpl(sharedPreferences)
        return ThemeUseCaseImpl(settingsRepository)
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl()
    }

    fun provideAudioPlayerStateRepository(context: Context): AudioPlayerStateRepository {
        val sharedPreferences =
            context.getSharedPreferences("player_state", Context.MODE_PRIVATE)
        return AudioPlayerStateRepositoryImpl(sharedPreferences)
    }

    fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        val sharedPreferences =
            context.getSharedPreferences("history_preferences", Context.MODE_PRIVATE)
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }
}