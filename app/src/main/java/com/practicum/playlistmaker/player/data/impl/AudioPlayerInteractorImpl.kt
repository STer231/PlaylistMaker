package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.search.domain.entity.Track

class AudioPlayerInteractorImpl : AudioPlayerInteractor {

    private var mediaPlayer: MediaPlayer? = null

    private val _onPrepared = MutableLiveData<Unit>()
    override val onPrepared: LiveData<Unit> = _onPrepared

    private val _onCompletion = MutableLiveData<Unit>()
    override val onCompletion: LiveData<Unit> = _onCompletion

    override fun prepare(track: Track) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(track.previewUrl)
            setOnPreparedListener { _onPrepared.postValue(Unit) }
            setOnCompletionListener { _onCompletion.postValue(Unit) }
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

