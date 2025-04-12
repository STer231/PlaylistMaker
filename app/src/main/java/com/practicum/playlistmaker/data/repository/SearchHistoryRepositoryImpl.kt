package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SearchHistoryRepository {

    companion object {
        private const val KEY_HISTORY_TRACKS = "key_for_new_track"
        private const val HISTORY_LIST_SIZE = 10
    }

    private val gson = Gson()

    override fun getTrackHistory(): List<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY_TRACKS, null)
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            gson.fromJson(json, Array<Track>::class.java).toList()
        }
    }

    override fun addToHistory(track: Track) {
        val historyList = getTrackHistory().toMutableList()
        historyList.removeAll { it.trackId == track.trackId }
        historyList.add(0, track)

        if (historyList.size > HISTORY_LIST_SIZE) {
            historyList.removeAt(historyList.lastIndex)
        }
        saveTrackHistory(historyList)
    }

    override fun clearTrackHistory() {
        sharedPreferences.edit()
            .remove(KEY_HISTORY_TRACKS)
            .apply()
    }

    private fun saveTrackHistory(tracks: List<Track>) {
        val json = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(KEY_HISTORY_TRACKS, json)
            .apply()
    }
}