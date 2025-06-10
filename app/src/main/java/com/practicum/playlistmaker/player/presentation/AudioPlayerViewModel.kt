package com.practicum.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.model.PlayerModel
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.model.TrackToPlayerModelMapper
import com.practicum.playlistmaker.search.domain.entity.Track

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var isPlaying = false

    companion object {
        private const val TIMER_DELAY = 300L

    }

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private lateinit var currentTrack: PlayerModel

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            if (isPlaying) {
                _playerState.postValue(
                    PlayerState.Playing(
                        currentTrack,
                        interactor.getCurrentPosition()
                    )
                )
                handler.postDelayed(this, TIMER_DELAY)
            }
        }
    }

    init {
        interactor.onPrepared.observeForever { onPrepared() }
        interactor.onCompletion.observeForever { onCompletion() }
    }

    fun loadTrack(track: Track) {
        currentTrack = TrackToPlayerModelMapper.map(track)
        interactor.prepare(track)
    }

    fun playbackControl() {
        if (isPlaying) pause() else start()
    }

    private fun start() {
        isPlaying = true
        interactor.startPlayback()
        _playerState.postValue(PlayerState.Playing(currentTrack, interactor.getCurrentPosition()))
        handler.post(updateTimerRunnable)
    }

    fun pause() {
        isPlaying = false
        interactor.pausePlayback()
        handler.removeCallbacks(updateTimerRunnable)
        _playerState.postValue(PlayerState.Paused(currentTrack, interactor.getCurrentPosition()))
    }

    private fun onPrepared() {
        _playerState.postValue(PlayerState.Ready(currentTrack))
    }

    private fun onCompletion() {
        isPlaying = false
        handler.removeCallbacks(updateTimerRunnable)
        _playerState.postValue(PlayerState.Completed(currentTrack))
    }

    override fun onCleared() {
        super.onCleared()
        interactor.releasePlayer()
        handler.removeCallbacks(updateTimerRunnable)
        interactor.onPrepared.removeObserver { onPrepared() }
        interactor.onCompletion.removeObserver { onCompletion() }
    }
}
