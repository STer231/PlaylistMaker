package com.practicum.playlistmaker.data.mapper

import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.data.dto.TrackDto

object TrackMapper {
    fun mapToDomain(dto: TrackDto): Track {
        return Track(
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTime = dto.trackTime,
            artworkUrl100 = dto.artworkUrl100,
            collectionName = dto.collectionName,
            releaseDate = dto.releaseDate,
            primaryGenreName = dto.primaryGenreName,
            country = dto.country,
            previewUrl = dto.previewUrl
        )
    }
}