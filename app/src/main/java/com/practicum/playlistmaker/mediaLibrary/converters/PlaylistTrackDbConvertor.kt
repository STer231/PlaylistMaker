package com.practicum.playlistmaker.mediaLibrary.converters

import com.practicum.playlistmaker.player.data.db.PlaylistTrackEntity
import com.practicum.playlistmaker.search.domain.entity.Track
import java.util.Date

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

    fun mapToDomain(trackEntity: PlaylistTrackEntity, isFavourite: Boolean = false): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTime = trackEntity.trackTime,
            artworkUrl100 = trackEntity.artworkUrl,
            collectionName = trackEntity.collectionName ?: "",
            releaseDate = Date(trackEntity.releaseDate),
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            previewUrl = trackEntity.previewUrl,
            isFavourite = isFavourite
        )
    }
}