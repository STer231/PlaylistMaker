package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.util.Resource
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}