package com.practicum.playlistmaker.playlist.search.domain.repository

import com.practicum.playlistmaker.playlist.search.domain.entity.Track

interface SearchHistoryRepository {
    fun getTrackHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearTrackHistory()
}