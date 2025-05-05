package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.entity.Track

interface SearchHistoryRepository {
    fun getTrackHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearTrackHistory()
}