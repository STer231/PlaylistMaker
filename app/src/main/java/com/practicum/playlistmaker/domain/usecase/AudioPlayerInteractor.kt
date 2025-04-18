package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.entity.Track

interface AudioPlayerInteractor {
    fun prepare(track: Track, onPrepared: () -> Unit)
    fun startPlayback()
    fun pausePlayback()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}