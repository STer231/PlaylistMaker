package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.mediaLibrary.presentation.CreatePlaylistViewModel
import com.practicum.playlistmaker.mediaLibrary.presentation.FavouritesViewModel
import com.practicum.playlistmaker.mediaLibrary.presentation.PlaylistsViewModel
import com.practicum.playlistmaker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
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
        AudioPlayerViewModel(get(), get())
    }

    // зависимости для экрана медиаплеер
    viewModel {
        FavouritesViewModel(get(), get())
    }

    viewModel {
        PlaylistsViewModel(get(), get())
    }

    // зависимости для экрана создания плейлистов
    viewModel {
        CreatePlaylistViewModel(get())
    }
}