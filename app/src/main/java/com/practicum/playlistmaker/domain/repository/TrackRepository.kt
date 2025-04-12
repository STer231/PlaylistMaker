package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.entity.Track

interface TrackRepository {
    fun searchTracks(expression: String): List<Track>
}