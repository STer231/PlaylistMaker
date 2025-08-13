package com.practicum.playlistmaker.mediaLibrary.presentation

data class CreatePlaylistState(
    val name: String = "",
    val description: String = "",
    val coverPath: String? = null,
    val canCreate: Boolean = false
)
