package com.practicum.playlistmaker.mediaLibrary.presentation

import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist

sealed interface PlaylistsState {
    data class Error(
        val message: String
    ) : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState
}