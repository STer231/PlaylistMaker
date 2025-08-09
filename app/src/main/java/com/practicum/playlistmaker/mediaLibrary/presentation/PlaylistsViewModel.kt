package com.practicum.playlistmaker.mediaLibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistInteractor
import com.practicum.playlistmaker.util.ErrorMessageProvider
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val errorMessageProvider: ErrorMessageProvider,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val playlistsState = MutableLiveData<PlaylistsState>()
    fun observePlaylistsState(): LiveData<PlaylistsState> = playlistsState

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect { list ->
                    if (list.isEmpty()) {
                        playlistsState.postValue(PlaylistsState.Error(errorMessageProvider.emptyPlaylists()))
                    } else {
                        playlistsState.postValue(PlaylistsState.Content(list))
                    }
                }
        }
    }
}