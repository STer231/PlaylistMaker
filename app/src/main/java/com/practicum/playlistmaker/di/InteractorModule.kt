package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.mediaLibrary.domain.repository.CreatePlaylistInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.usecase.CreatePlaylistInteractorImpl
import com.practicum.playlistmaker.player.data.impl.FavouriteInteractorImpl
import com.practicum.playlistmaker.player.data.repository.FavouriteInteractor
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.search.domain.usecase.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.usecase.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.usecase.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.usecase.SearchTracksInteractorImpl
import com.practicum.playlistmaker.util.ErrorMessageProvider
import com.practicum.playlistmaker.util.ErrorMessageProviderImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {

    // зависимости для экрана поиск
    single<SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<ErrorMessageProvider> {
        ErrorMessageProviderImpl(androidContext())
    }

    // зависимости для экрана настроек
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    // зависимости для экрана аудиоплеер
    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    // зависимости для базы данных
    single<FavouriteInteractor> {
        FavouriteInteractorImpl(get())
    }

    factory<CreatePlaylistInteractor> {
        CreatePlaylistInteractorImpl(get())
    }
}