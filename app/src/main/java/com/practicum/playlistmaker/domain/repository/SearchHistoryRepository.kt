package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.entity.Track

interface SearchHistoryRepository {
    fun getTrackHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearTrackHistory()
}