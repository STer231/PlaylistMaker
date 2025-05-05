package com.practicum.playlistmaker.player.domain.usecase

import com.practicum.playlistmaker.search.domain.entity.Track

interface AudioPlayerInteractor {
    fun prepare(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayback()
    fun pausePlayback()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}
