package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer
) : AudioPlayerRepository {

    private val _onPrepared = MutableLiveData<Unit>()
    override val onPrepared: LiveData<Unit> = _onPrepared

    private val _onCompletion = MutableLiveData<Unit>()
    override val onCompletion: LiveData<Unit> = _onCompletion

    override fun prepare(previewUrl: String) {
        mediaPlayer.reset()
        mediaPlayer.apply {
            setDataSource(previewUrl)
            setOnPreparedListener { _onPrepared.postValue(Unit) }
            setOnCompletionListener { _onCompletion.postValue(Unit) }
            prepareAsync()
        }
    }

    override fun start() = mediaPlayer.start()

    override fun pause() = mediaPlayer.pause()

    override fun release() = mediaPlayer.release()

    override fun currentPosition(): Int = mediaPlayer.currentPosition
}