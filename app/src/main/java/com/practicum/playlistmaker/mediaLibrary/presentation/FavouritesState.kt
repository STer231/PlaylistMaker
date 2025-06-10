package com.practicum.playlistmaker.mediaLibrary.presentation

sealed interface FavouritesState {
    data class Error(
        val message: String
    ) : FavouritesState
}