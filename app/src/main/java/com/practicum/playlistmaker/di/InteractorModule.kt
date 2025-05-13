package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.search.ui.ErrorMessageProvider
import com.practicum.playlistmaker.search.ui.ErrorMessageProviderImpl
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
    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl()
    }
}