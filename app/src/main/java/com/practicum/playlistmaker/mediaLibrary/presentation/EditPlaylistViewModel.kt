package com.practicum.playlistmaker.mediaLibrary.presentation

import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistInteractor
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistId: Long
): CreatePlaylistViewModel(playlistInteractor) {

    private var currentPlaylist: Playlist? = null

    init {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(playlistId).firstOrNull()
            playlist?.let {
                currentPlaylist = it
                updateState { oldState ->
                    oldState.copy(
                        name = it.name,
                        description = it.description ?: "",
                        coverPath = it.pathImageFile,
                        canCreate = it.name.isNotBlank()
                    )
                }
            }
        }
    }

    override fun createPlaylist(onCompleted: () -> Unit) {
        val state = _createPlaylistState.value ?: return
        val oldPlaylist = currentPlaylist ?: return

        viewModelScope.launch {
            val savePath = selectedCoverUri?.let {
                playlistInteractor.saveImageToPrivateStorage(it)
            } ?: oldPlaylist.pathImageFile

            val newPlaylist = Playlist(
                id = oldPlaylist.id,
                name = state.name,
                description = state.description,
                pathImageFile = savePath,
                trackIds = oldPlaylist.trackIds,
                playlistSize = oldPlaylist.playlistSize
            )

            playlistInteractor.createPlaylist(newPlaylist)
            onCompleted()
        }
    }
}