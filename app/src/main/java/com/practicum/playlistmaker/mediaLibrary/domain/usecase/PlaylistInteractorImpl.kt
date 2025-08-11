package com.practicum.playlistmaker.mediaLibrary.domain.usecase

import android.net.Uri
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistRepository
import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Locale

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val externalNavigator: ExternalNavigator
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

    override suspend fun sharePlaylist(playlistId: Long): Boolean {
        val playlist = playlistRepository.getPlaylistById(playlistId).firstOrNull() ?: return false

        val trackIds = playlist.trackIds
        val tracks: List<Track> = if (trackIds.isEmpty()) {
            emptyList()
        } else {
            playlistRepository.getTracksByIds(trackIds).firstOrNull() ?: emptyList()
        }
        if (tracks.isEmpty()) return false

        val text = buildShareText(playlist, tracks)
        externalNavigator.shareText(text)
        return true
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }

    private fun formatDuration(millis: Int): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    private fun buildShareText(playlist: Playlist, tracks: List<Track>) : String {
        val builder = StringBuilder()
        builder.append(playlist.name).append("\n")
        playlist.description?.takeIf { it.isNotBlank() }?.let {
            builder.append(it).append("\n")
        }
        builder.append("${tracks.size} треков").append("\n")
        tracks.forEachIndexed { index, track ->
            val duration = formatDuration(track.trackTime)
            builder.append("${index + 1}. ${track.artistName} - ${track.trackName} ($duration)").append("\n")
        }
        return builder.toString()
    }
}