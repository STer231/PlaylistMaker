package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.mediaLibrary.data.impl.CreatePlaylistRepositoryImpl
import com.practicum.playlistmaker.mediaLibrary.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.data.impl.FavouriteRepositoryImpl
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepository
import com.practicum.playlistmaker.player.data.repository.FavouriteRepository
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.repository.TrackRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    // зависимости для экрана поиск
    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named("history_preferences")), get())
    }

    // зависимости для экрана настроек
    single<SettingsRepository> {
        SettingsRepositoryImpl(get(named("theme_preferences")))
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    // зависимости для экрана аудиоплеер
    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    // зависимости для базы данных
    single<FavouriteRepository> {
        FavouriteRepositoryImpl(get(), get())
    }

    factory<CreatePlaylistRepository> {
        CreatePlaylistRepositoryImpl(get(), get(), get(), get(), androidContext())
    }
}