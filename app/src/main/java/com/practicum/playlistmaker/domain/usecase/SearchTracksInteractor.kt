package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.entity.Track

interface SearchTracksInteractor {
    fun search(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(tracks: List<Track>)
    }
}