package com.practicum.playlistmaker.playlist.player.ui

import com.practicum.playlistmaker.playlist.player.domain.model.PlayerModel

data class PlayerViewState(
    val track: PlayerModel? = null,
    val isPlayEnabled: Boolean = false,
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0
)
