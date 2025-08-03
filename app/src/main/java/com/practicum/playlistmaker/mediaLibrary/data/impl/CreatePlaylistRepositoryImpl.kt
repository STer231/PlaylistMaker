package com.practicum.playlistmaker.mediaLibrary.data.impl

import com.practicum.playlistmaker.mediaLibrary.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.mediaLibrary.data.db.PlaylistDao
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.CreatePlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreatePlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConvertor: PlaylistDbConvertor
): CreatePlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist): Long {
        return playlistDao.insertPlaylist(playlistDbConvertor.mapToEntity(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
       return playlistDao.getPlaylists().map { entityList ->
           entityList.map { playlistEntity -> playlistDbConvertor.mapToDomain(playlistEntity) }
       }
    }
}