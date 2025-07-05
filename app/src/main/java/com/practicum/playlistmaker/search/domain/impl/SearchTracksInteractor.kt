package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksInteractor {
    fun search(query: String): Flow<Pair<List<Track>?, String?>>
}