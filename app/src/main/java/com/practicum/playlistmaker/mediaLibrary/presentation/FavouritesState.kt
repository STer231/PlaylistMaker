package com.practicum.playlistmaker.mediaLibrary.presentation

import com.practicum.playlistmaker.search.domain.entity.Track

sealed interface FavouritesState {

    data class Content(
        val favouriteTracks: List<Track>
    ) : FavouritesState

    data class Error(
        val message: String
    ) : FavouritesState
}