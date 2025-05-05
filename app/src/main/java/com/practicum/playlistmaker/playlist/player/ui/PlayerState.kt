package com.practicum.playlistmaker.playlist.player.ui

import com.practicum.playlistmaker.playlist.player.domain.model.PlayerModel

sealed interface PlayerState {
    data class Ready(val track: PlayerModel) : PlayerState
    data class Playing(val track: PlayerModel, val currentPosition: Int) : PlayerState
    data class Paused(val track: PlayerModel, val currentPosition: Int) : PlayerState
    data class Completed(val track: PlayerModel) : PlayerState
}
