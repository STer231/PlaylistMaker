package com.practicum.playlistmaker.player

import com.practicum.playlistmaker.player.domain.model.PlayerModel

sealed interface PlayerState {
    data class Ready(val track: PlayerModel) : PlayerState
    data class Playing(val track: PlayerModel, val currentPosition: Int) : PlayerState
    data class Paused(val track: PlayerModel, val currentPosition: Int) : PlayerState
    data class Completed(val track: PlayerModel) : PlayerState
}
