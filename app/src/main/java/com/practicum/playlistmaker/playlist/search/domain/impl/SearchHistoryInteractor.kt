package com.practicum.playlistmaker.playlist.search.domain.impl

import com.practicum.playlistmaker.playlist.search.domain.entity.Track

interface SearchHistoryInteractor {
    fun getHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
}