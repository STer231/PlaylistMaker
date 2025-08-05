package com.practicum.playlistmaker.mediaLibrary.domain.repository

import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist): Long

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

}