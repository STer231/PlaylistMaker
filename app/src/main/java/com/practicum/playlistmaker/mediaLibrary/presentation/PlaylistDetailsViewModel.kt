package com.practicum.playlistmaker.mediaLibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistInteractor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlistDetailsState = MutableLiveData<PlaylistDetailsState>()
    val playlistDetailsState: LiveData<PlaylistDetailsState> = _playlistDetailsState

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId)
                .collectLatest { playlist ->
                    if (playlist == null) {
                        _playlistDetailsState.postValue(
                            PlaylistDetailsState(
                                playlist = null,
                                tracks = emptyList(),
                                durationTrackSum = 0L
                            )
                        )
                    } else {
                        val tracks = if (playlist.trackIds.isEmpty()) {
                            emptyList()
                        } else {
                            playlistInteractor.getTracksByIds(playlist.trackIds).first()
                        }

                        val durationMillsSum = tracks.sumOf { it.trackTime.toLong() }

                        _playlistDetailsState.postValue(
                            PlaylistDetailsState(
                                playlist = playlist,
                                tracks = tracks,
                                durationTrackSum = durationMillsSum
                            )
                        )
                    }
                }
        }
        }

    fun removeTrack(trackId: Int) {
        val playlist = _playlistDetailsState.value?.playlist ?: return
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(trackId, playlist)
        }
    }

        fun formatMinutesFromMillis(millis: Long): String {
            val totalMinutes = millis / 60000
            return "$totalMinutes минут"
        }

    }