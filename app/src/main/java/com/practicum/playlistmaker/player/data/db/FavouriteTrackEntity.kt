package com.practicum.playlistmaker.player.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_tracks")
data class FavouriteTrackEntity(
    @PrimaryKey()
    val trackId: Int,
    val artworkUrl: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: Long,
    val primaryGenreName: String,
    val country: String,
    val trackTime: Int,
    val previewUrl: String,
    val lastAdded: Long = System.currentTimeMillis()
)
