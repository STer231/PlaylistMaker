package com.practicum.playlistmaker.search.domain.usecase

import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.util.Resource
import com.practicum.playlistmaker.search.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksInteractorImpl(private val repository: TrackRepository) : SearchTracksInteractor {

    override fun search(query: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(query).map { result ->
            when(result) {
                is Resource.Success -> { Pair(result.data, null)}
                is Resource.Error -> { Pair(null, result.message)}
            }
        }
    }
}

