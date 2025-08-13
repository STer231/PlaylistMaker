package com.practicum.playlistmaker.mediaLibrary.presentation

import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.entity.Track

data class PlaylistDetailsState(
    val playlist: Playlist? = null,
    val tracks: List<Track> = emptyList(),
    val durationTrackSum: Long = 0
)