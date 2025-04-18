package com.practicum.playlistmaker.domain.usecase

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.entity.Track

class AudioPlayerInteractorImpl : AudioPlayerInteractor {

    private var mediaPlayer: MediaPlayer? = null

    override fun prepare(track: Track, onPrepared: () -> Unit) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(track.previewUrl)
            setOnPreparedListener { onPrepared() }
            prepareAsync()
        }
    }

    override fun startPlayback() {
        mediaPlayer?.start()
    }

    override fun pausePlayback() {
        mediaPlayer?.pause()
    }

    override fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
}