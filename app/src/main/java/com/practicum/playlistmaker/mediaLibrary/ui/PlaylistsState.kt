package com.practicum.playlistmaker.mediaLibrary.ui

sealed interface PlaylistsState {
    data class Error(
        val message: String
    ) : PlaylistsState
}