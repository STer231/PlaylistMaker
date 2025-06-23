package com.practicum.playlistmaker.mediaLibrary.presentation

sealed interface PlaylistsState {
    data class Error(
        val message: String
    ) : PlaylistsState
}