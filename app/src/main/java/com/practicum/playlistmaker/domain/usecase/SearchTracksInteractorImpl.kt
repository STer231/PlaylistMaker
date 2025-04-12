package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.repository.TrackRepository

class SearchTracksInteractorImpl(private val repository: TrackRepository) : SearchTracksInteractor {
    override fun search(query: String, consumer: SearchTracksInteractor.TrackConsumer) {
        Thread {
            val tracks = repository.searchTracks(query)
            consumer.consume(tracks)
        }.start()
    }
}