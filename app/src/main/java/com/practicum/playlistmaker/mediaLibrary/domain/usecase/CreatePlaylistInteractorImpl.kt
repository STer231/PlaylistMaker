package com.practicum.playlistmaker.mediaLibrary.domain.usecase

import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.CreatePlaylistInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

class CreatePlaylistInteractorImpl(
    private val createPlaylistRepository: CreatePlaylistRepository
) : CreatePlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        return createPlaylistRepository.createPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return createPlaylistRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        createPlaylistRepository.addTrackToPlaylist(track, playlist)
    }
}