package com.practicum.playlistmaker.mediaLibrary.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.mediaLibrary.data.db.PlaylistEntity
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist

class PlaylistDbConvertor(
    private val gson: Gson
) {

    private val typeToken = object : TypeToken<List<Long>>() {}.type

    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            pathImageFile = playlist.pathImageFile,
            trackIdsJson = fromList(playlist.trackIds),
            playlistSize = playlist.playlistSize,
        )
    }

    fun mapToDomain(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            pathImageFile = playlistEntity.pathImageFile,
            trackIds = toList(playlistEntity.trackIdsJson),
            playlistSize = playlistEntity.playlistSize,
        )
    }

    private fun fromList(list: List<Long>): String {
        return gson.toJson(list)
    }

    private fun toList(json: String): List<Long> {
        return gson.fromJson(json, typeToken) ?: emptyList()
    }
}