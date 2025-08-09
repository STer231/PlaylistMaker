package com.practicum.playlistmaker.mediaLibrary.domain.repository

import android.net.Uri
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun saveImageToPrivateStorage(uri: Uri): String?

    fun getPlaylistById(id: Long): Flow<Playlist?>

    fun getTracksByIds(id: List<Long>): Flow<List<Track>>
}