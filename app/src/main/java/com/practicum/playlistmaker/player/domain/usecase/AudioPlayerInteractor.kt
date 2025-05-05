package com.practicum.playlistmaker.player.domain.usecase

import androidx.lifecycle.LiveData
import com.practicum.playlistmaker.search.domain.entity.Track

interface AudioPlayerInteractor {
    val onPrepared: LiveData<Unit>
    val onCompletion: LiveData<Unit>

    fun prepare(track: Track)
    fun startPlayback()
    fun pausePlayback()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}
