package com.practicum.playlistmaker.mediaLibrary.data.impl

import com.practicum.playlistmaker.mediaLibrary.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.mediaLibrary.converters.PlaylistTrackDbConvertor
import com.practicum.playlistmaker.mediaLibrary.data.db.PlaylistDao
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmaker.player.data.db.PlaylistTrackDao
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CreatePlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val playlistTrackDao: PlaylistTrackDao,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor
) : CreatePlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        return playlistDao.insertPlaylist(playlistDbConvertor.mapToEntity(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().map { entityList ->
            entityList.map { playlistEntity -> playlistDbConvertor.mapToDomain(playlistEntity) }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistTrackDao.insertTrack(playlistTrackDbConvertor.mapToEntity(track))

            val newIds = playlist.trackIds + track.trackId.toLong()
            val newSize = playlist.playlistSize + 1

            val updated = playlist.copy(trackIds = newIds, playlistSize = newSize)
            playlistDao.insertPlaylist(playlistDbConvertor.mapToEntity(updated))
        }
    }
}