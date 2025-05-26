package com.practicum.playlistmaker.mediaLibrary.ui

sealed interface FavouriteState {

    data class Error(
        val message: String
    ) : FavouriteState
}