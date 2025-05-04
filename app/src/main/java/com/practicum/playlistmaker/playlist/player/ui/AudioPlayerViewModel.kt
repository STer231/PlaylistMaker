package com.practicum.playlistmaker.playlist.player.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.playlist.creator.Creator
import com.practicum.playlistmaker.playlist.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.playlist.player.domain.model.TrackToPlayerModelMapper
import com.practicum.playlistmaker.playlist.search.domain.entity.Track

class AudioPlayerViewModel(
    application: Application,
    private val interactor: AudioPlayerInteractor
) : AndroidViewModel(application) {

    private val handler = Handler(Looper.getMainLooper())

    private var isPlaying = false

    companion object {

        private const val TIMER_DELAY = 300L

        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    AudioPlayerViewModel(
                        this[APPLICATION_KEY] as Application,
                        Creator.provideAudioPlayerInteractor()
                    )
                }
            }
    }

    private val _playerState = MutableLiveData<PlayerViewState>()
    val playerState: LiveData<PlayerViewState> = _playerState

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            val position = interactor.getCurrentPosition()
            _playerState.value = _playerState.value
                ?.copy(currentPosition = position)
            if (isPlaying) {
                handler.postDelayed(this, TIMER_DELAY)
            }
        }
    }

    fun loadTrack(track: Track) {
        interactor.prepare(
            track,
            onPrepared = {
                _playerState.postValue(
                    PlayerViewState(
                        track = TrackToPlayerModelMapper.map(track),
                        isPlayEnabled = true,
                        isPlaying = false,
                        currentPosition = 0
                    )
                )
            },
            onCompletion = {
                isPlaying = false
                handler.removeCallbacks(updateTimerRunnable)
                _playerState.postValue(
                    _playerState.value?.copy(
                        isPlaying = false,
                        currentPosition = 0
                    )
                )
            }
        )
    }

    fun playbackControl() {
        if (isPlaying) pause() else start()
    }

    private fun start() {
        isPlaying = true
        interactor.startPlayback()
        _playerState.value = _playerState.value?.copy(isPlaying = true)
        handler.post(updateTimerRunnable)
    }

    fun pause() {
        isPlaying = false
        interactor.pausePlayback()
        handler.removeCallbacks(updateTimerRunnable)
        _playerState.value = _playerState.value?.copy(isPlaying = false)
    }

    override fun onCleared() {
        super.onCleared()
        interactor.releasePlayer()
        handler.removeCallbacks(updateTimerRunnable)
    }
}
