package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.ui.AudioPlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // зависимости для экрана поиск
    viewModel {
        SearchViewModel(get(), get(), get())
    }

    // зависимости для экрана настроек
    viewModel {
        SettingsViewModel(get(), get())
    }

    // зависимости для экрана аудиоплеер
    viewModel {
        AudioPlayerViewModel(get())
    }
}