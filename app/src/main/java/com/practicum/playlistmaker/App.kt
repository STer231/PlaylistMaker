package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.di.dataModule
import com.practicum.playlistmaker.search.di.interactorModule
import com.practicum.playlistmaker.search.di.repositoryModule
import com.practicum.playlistmaker.search.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object PreferencesKeys {
        const val PREFERENCES_NAME = "theme_preferences"
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule,
            )
        }

        val settingsInteractor = Creator.provideSettingsInteractor(this)
        val isDark = settingsInteractor.loadTheme().isDarkMode

        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

    }
}