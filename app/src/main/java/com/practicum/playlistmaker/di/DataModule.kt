package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.network.ItunesApi
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.sharing.data.impl.SharingConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    // зависимости для экрана поиск
    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single(named("history_preferences")) {
        androidContext().getSharedPreferences(
            SearchHistoryRepositoryImpl.PREFERENCES_HISTORY,
            Context.MODE_PRIVATE
        )
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    // зависимости для экрана настроек
    single(named("theme_preferences")) {
        androidContext().getSharedPreferences(
            SettingsRepositoryImpl.PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    single {
        SharingConfig(androidContext())
    }

    // зависимости для экрана аудиоплеера
    factory { MediaPlayer() }
}