package com.practicum.playlistmaker.playlist.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.playlist.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.playlist.search.domain.entity.Track

class AudioPlayerInteractorImpl : AudioPlayerInteractor {

    private var mediaPlayer: MediaPlayer? = null

    override fun prepare(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(track.previewUrl)
            setOnPreparedListener { onPrepared() }
            setOnCompletionListener { onCompletion() }
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

