package com.practicum.playlistmaker.player.data.repository

import androidx.lifecycle.LiveData

interface AudioPlayerRepository {
    val onPrepared: LiveData<Unit>
    val onCompletion: LiveData<Unit>

    fun prepare(previewUrl: String)
    fun start()
    fun pause()
    fun release()
    fun currentPosition(): Int
}