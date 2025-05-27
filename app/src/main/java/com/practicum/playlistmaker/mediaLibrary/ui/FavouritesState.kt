package com.practicum.playlistmaker.mediaLibrary.ui

sealed interface FavouritesState {
    data class Error(
        val message: String
    ) : FavouritesState
}