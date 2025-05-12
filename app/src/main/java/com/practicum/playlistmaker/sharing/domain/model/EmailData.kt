package com.practicum.playlistmaker.sharing.domain.model

data class EmailData(
    val receivers: Array<String>,
    val subject: String,
    val body: String
)
