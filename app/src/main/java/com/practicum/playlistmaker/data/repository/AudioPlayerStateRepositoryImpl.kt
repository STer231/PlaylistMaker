package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.repository.AudioPlayerStateRepository

class AudioPlayerStateRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : AudioPlayerStateRepository {

    companion object {
        private const val KEY_PLAYER_STATE = "is_player_screen_active"
    }

    override fun isPlayerActive(): Boolean {
        return sharedPreferences.getBoolean(KEY_PLAYER_STATE, false)
    }

    override fun setPlayerActive(isActive: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_PLAYER_STATE, isActive)
            .apply()
    }
}