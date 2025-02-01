package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    companion object PreferencesKeysHistory {
        const val PREFERENCES_HISTORY = "history_preferences"
        const val KEY_HISTORY_TRACKS = "key_for_new_track"
    }

    private val gson = Gson()

    fun saveTracksHistory(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(KEY_HISTORY_TRACKS, json)
            .apply()
    }

    fun getTracksHistory(): List<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY_TRACKS, null)
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            gson.fromJson(json, Array<Track>::class.java).toList()
        }
    }

    fun clearTracksHistory() {
        sharedPreferences.edit()
            .remove(KEY_HISTORY_TRACKS)
            .apply()
    }

    fun addToHistory(track: Track) {
        val historyList = getTracksHistory().toMutableList()

        if (!historyList.any { it.trackId == track.trackId }) {
            historyList.add(0, track)
        }
        if (historyList.size >10) {
            historyList.removeLast()
        }
        saveTracksHistory(historyList)
    }
}