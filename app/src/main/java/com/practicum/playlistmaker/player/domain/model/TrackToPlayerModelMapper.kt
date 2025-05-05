package com.practicum.playlistmaker.player.domain.model

import com.practicum.playlistmaker.search.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackToPlayerModelMapper {

    private val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    fun map(track: Track): PlayerModel {
        return PlayerModel(
            trackName = track.trackName,
            artistName = track.artistName,
            formattedDuration = timeFormatter.format(track.trackTime),
            collectionName = track.collectionName,
            formattedReleaseYear = yearFormatter.format(track.releaseDate),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            artworkUrl = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        )
    }

    fun mapDuration(positionMs: Int): String {
        return timeFormatter.format(positionMs.toLong())
    }
}