package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.Resource
import com.practicum.playlistmaker.search.domain.entity.Track

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}