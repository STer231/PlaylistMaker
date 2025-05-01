package com.practicum.playlistmaker.playlist.search.domain.impl

import com.practicum.playlistmaker.playlist.creator.Resource
import com.practicum.playlistmaker.playlist.search.domain.repository.TrackRepository
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(private val repository: TrackRepository) : SearchTracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(query: String, consumer: SearchTracksInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(query)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}

