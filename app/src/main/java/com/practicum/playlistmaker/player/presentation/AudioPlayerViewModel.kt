package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.data.repository.FavouriteInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerModel
import com.practicum.playlistmaker.player.domain.usecase.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.model.TrackToPlayerModelMapper
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
    private val favouriteInteractor: FavouriteInteractor,
    ) : ViewModel() {

    private var isPlaying = false

    private var timerJob: Job? = null

    companion object {
        private const val TIMER_DELAY = 300L
    }

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    private lateinit var currentPlayerTrack: PlayerModel

    private lateinit var currentDomainTrack: Track

    init {
        playerInteractor.onPrepared.observeForever { onPrepared() }
        playerInteractor.onCompletion.observeForever { onCompletion() }
    }

    fun loadTrack(track: Track) {
        currentDomainTrack = track
        currentPlayerTrack = TrackToPlayerModelMapper.map(track)
        viewModelScope.launch {
            val favourite = favouriteInteractor.isTrackFavourite(track.trackId)
            currentDomainTrack.isFavourite = favourite
            _isFavourite.postValue(favourite)
        }
        playerInteractor.prepare(track)
    }

    fun playbackControl() {
        if (isPlaying) pause() else start()
    }

    private fun start() {
        isPlaying = true
        playerInteractor.startPlayback()
        _playerState.postValue(PlayerState.Playing(currentPlayerTrack, playerInteractor.getCurrentPosition()))
        startTimer()
    }

    fun pause() {
        isPlaying = false
        playerInteractor.pausePlayback()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Paused(currentPlayerTrack, playerInteractor.getCurrentPosition()))
    }

    private fun onPrepared() {
        _playerState.postValue(PlayerState.Ready(currentPlayerTrack))
    }

    private fun onCompletion() {
        isPlaying = false
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Completed(currentPlayerTrack))
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
        timerJob?.cancel()
        playerInteractor.onPrepared.removeObserver { onPrepared() }
        playerInteractor.onCompletion.removeObserver { onCompletion() }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (isPlaying) {
                delay(TIMER_DELAY)
                _playerState.postValue(PlayerState.Playing(currentPlayerTrack, playerInteractor.getCurrentPosition()))
            }
        }
    }

    fun onFavouriteClicked() {
        viewModelScope.launch {
            val currentFavourite = _isFavourite.value == true
            if (currentFavourite) {
                favouriteInteractor.deleteFromFavourite(currentDomainTrack)
            } else {
                favouriteInteractor.addToFavourite(currentDomainTrack)
            }
            _isFavourite.postValue(!currentFavourite)
        }
    }
}
