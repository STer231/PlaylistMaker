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
import com.practicum.playlistmaker.playlist.player.domain.model.PlayerModel
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

    fun loadTrack(track: Track) {

        interactor.prepare(
            track,
            onPrepared = {
                currentTrack = TrackToPlayerModelMapper.map(track)
                _playerState.postValue(PlayerState.Ready(currentTrack))
            },
            onCompletion = {
                isPlaying = false
                handler.removeCallbacks(updateTimerRunnable)
                _playerState.postValue(PlayerState.Completed(currentTrack))
            }
        )
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

    override fun onCleared() {
        super.onCleared()
        interactor.releasePlayer()
        handler.removeCallbacks(updateTimerRunnable)
    }
}
