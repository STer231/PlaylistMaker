package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.player.data.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractor

object Creator {

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl()
    }
}