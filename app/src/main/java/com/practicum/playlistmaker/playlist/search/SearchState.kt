package com.practicum.playlistmaker.playlist.search

import com.practicum.playlistmaker.playlist.search.domain.entity.Track

sealed interface SearchState {
    object Initial: SearchState
    object Loading : SearchState
    data class Content(val tracks: List<Track>) : SearchState
    data class Empty(val message: String) : SearchState
    data class Error(val errorMessage: String) : SearchState
}