package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.entity.Track

interface SearchTracksInteractor {
    fun search(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}