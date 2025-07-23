package com.practicum.playlistmaker.player.data.converters

import com.practicum.playlistmaker.player.data.db.FavouriteTrackEntity
import com.practicum.playlistmaker.search.domain.entity.Track
import java.util.Date

class TrackDbConvertor {

    fun mapToEntity(track: Track): FavouriteTrackEntity {
        return FavouriteTrackEntity(
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

    fun mapToDomain(track: FavouriteTrackEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl,
            collectionName = track.collectionName,
            releaseDate = Date(track.releaseDate),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        ).also {
            it.isFavourite = true
        }
    }
}