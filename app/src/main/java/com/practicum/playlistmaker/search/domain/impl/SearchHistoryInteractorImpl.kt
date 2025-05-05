package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(
    private val historyRepository: SearchHistoryRepository
) : SearchHistoryInteractor {
    override fun getHistory(): List<Track> {
        return historyRepository.getTrackHistory()
    }

    override fun addToHistory(track: Track) {
        historyRepository.addToHistory(track)
    }

    override fun clearHistory() {
        historyRepository.clearTrackHistory()
    }
}