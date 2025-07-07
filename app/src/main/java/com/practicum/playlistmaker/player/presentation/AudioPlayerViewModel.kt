package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.model.PlayerModel
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.model.TrackToPlayerModelMapper
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor
) : ViewModel() {

    private var isPlaying = false

    private var timerJob: Job? = null

    companion object {
        private const val TIMER_DELAY = 300L
    }

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private lateinit var currentTrack: PlayerModel

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
        startTimer()
    }

    fun pause() {
        isPlaying = false
        interactor.pausePlayback()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Paused(currentTrack, interactor.getCurrentPosition()))
    }

    private fun onPrepared() {
        _playerState.postValue(PlayerState.Ready(currentTrack))
    }

    private fun onCompletion() {
        isPlaying = false
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Completed(currentTrack))
    }

    override fun onCleared() {
        super.onCleared()
        interactor.releasePlayer()
        timerJob?.cancel()
        interactor.onPrepared.removeObserver { onPrepared() }
        interactor.onCompletion.removeObserver { onCompletion() }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (isPlaying) {
                delay(TIMER_DELAY)
                _playerState.postValue(PlayerState.Playing(currentTrack, interactor.getCurrentPosition()))
            }
        }
    }
}
