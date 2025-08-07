package com.practicum.playlistmaker.mediaLibrary.data.impl

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.practicum.playlistmaker.mediaLibrary.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.mediaLibrary.converters.PlaylistTrackDbConvertor
import com.practicum.playlistmaker.mediaLibrary.data.db.PlaylistDao
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmaker.player.data.db.PlaylistTrackDao
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CreatePlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val playlistTrackDao: PlaylistTrackDao,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor,
    private val context: Context
) : CreatePlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        return playlistDao.insertPlaylist(playlistDbConvertor.mapToEntity(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().distinctUntilChanged().map { entityList ->
            entityList.map { playlistEntity -> playlistDbConvertor.mapToDomain(playlistEntity) }
        }
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

    override suspend fun saveImageToPrivateStorage(uri: Uri): String? = withContext(Dispatchers.IO) {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlist_cover"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "cover_${System.currentTimeMillis()}.jpg")

         try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: IOException) {
            Log.e("CreatePlaylistFragment", "Ошибка копирования обложки", e)
            null
        }
    }
}