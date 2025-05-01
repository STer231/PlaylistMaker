package com.practicum.playlistmaker.playlist.search.domain.repository

import com.practicum.playlistmaker.playlist.creator.Resource
import com.practicum.playlistmaker.playlist.search.domain.entity.Track

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}