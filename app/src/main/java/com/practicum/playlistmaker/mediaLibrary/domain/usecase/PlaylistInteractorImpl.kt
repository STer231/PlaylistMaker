package com.practicum.playlistmaker.mediaLibrary.domain.usecase

import android.net.Uri
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistRepository
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        return playlistRepository.createPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): String? {
        return playlistRepository.saveImageToPrivateStorage(uri)
    }

    override fun getPlaylistById(id: Long): Flow<Playlist?> {
        return playlistRepository.getPlaylistById(id)
    }

    override fun getTracksByIds(id: List<Long>): Flow<List<Track>> {
        return playlistRepository.getTracksByIds(id)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlist: Playlist) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlist)
    }
}