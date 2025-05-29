package com.practicum.playlistmaker.mediaLibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.util.ErrorMessageProvider

class PlaylistsViewModel(
    private val errorMessageProvider: ErrorMessageProvider
) : ViewModel() {

    private val playlistsState = MutableLiveData<PlaylistsState>()
    fun observePlaylistsState(): LiveData<PlaylistsState> = playlistsState

    init {
        playlistsState.postValue(PlaylistsState.Error(errorMessageProvider.emptyPlaylists()))
    }
}