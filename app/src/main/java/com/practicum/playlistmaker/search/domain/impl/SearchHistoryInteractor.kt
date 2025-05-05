package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.entity.Track

interface SearchHistoryInteractor {
    fun getHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
}