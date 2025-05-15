package com.practicum.playlistmaker.player.domain.model

data class PlayerModel(
    val trackName: String,
    val artistName: String,
    val formattedDuration: String,
    val collectionName: String,
    val formattedReleaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl: String
)
