package com.practicum.playlistmaker.mediaLibrary.domain.repository

import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist): Long

    fun getPlaylists(): Flow<List<Playlist>>

}