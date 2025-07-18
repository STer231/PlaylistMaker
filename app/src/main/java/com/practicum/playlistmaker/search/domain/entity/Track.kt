package com.practicum.playlistmaker.search.domain.entity

import java.util.Date

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavourite: Boolean = false
)