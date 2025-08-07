package com.practicum.playlistmaker.search.domain.usecase

import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksInteractor {
    fun search(query: String): Flow<Pair<List<Track>?, String?>>
}