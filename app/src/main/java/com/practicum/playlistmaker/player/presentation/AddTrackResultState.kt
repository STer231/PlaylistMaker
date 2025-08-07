package com.practicum.playlistmaker.player.presentation

sealed interface AddTrackResultState {

    data class Added(
        val playlistName: String
    ) : AddTrackResultState

    data class AlreadyHas(
        val playlistName: String
    ) : AddTrackResultState
}