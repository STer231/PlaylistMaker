package com.practicum.playlistmaker.mediaLibrary.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val pathImageFile: String? = null,
    val trackIds: List<Long> = emptyList(),
    val playlistSize: Int = 0
) : Parcelable
