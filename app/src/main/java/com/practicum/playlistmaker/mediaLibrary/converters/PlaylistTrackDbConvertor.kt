package com.practicum.playlistmaker.mediaLibrary.converters

import com.practicum.playlistmaker.player.data.db.PlaylistTrackEntity
import com.practicum.playlistmaker.search.domain.entity.Track

class PlaylistTrackDbConvertor {

    fun mapToEntity(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            trackId = track.trackId,
            artworkUrl = track.artworkUrl100,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate.time,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTime = track.trackTime,
            previewUrl = track.previewUrl
        )
    }
}