package com.practicum.playlistmaker.domain.repository

interface AudioPlayerStateRepository {
    fun isPlayerActive(): Boolean
    fun setPlayerActive(isActive: Boolean)
}