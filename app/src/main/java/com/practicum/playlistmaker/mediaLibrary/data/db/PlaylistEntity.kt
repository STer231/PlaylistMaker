package com.practicum.playlistmaker.mediaLibrary.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String?,
    val pathImageFile: String?,
    val trackIdsJson: String,
    val playlistSize: Int
)
