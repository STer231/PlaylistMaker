package com.practicum.playlistmaker.playlist.sharing.domain.model

data class EmailData(
    val receivers: Array<String>,
    val subject: String,
    val body: String
)
