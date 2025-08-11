package com.practicum.playlistmaker.mediaLibrary.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistInteractor
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    protected var selectedCoverUri: Uri? = null

    protected val _createPlaylistState = MutableLiveData<CreatePlaylistState>()
    val createPlaylistState: LiveData<CreatePlaylistState> = _createPlaylistState

    fun onNameChange(name: String) {
        updateState { oldState ->
            oldState.copy(
                name = name,
                canCreate = name.isNotBlank()
            )
        }
    }

    fun onDescriptionChange(description: String) {
        updateState { oldState ->
            oldState.copy(
                description = description
            )
        }
    }

    fun onCoverSelected(uri: Uri) {
        selectedCoverUri = uri
        updateState { oldState ->
            oldState.copy(
                coverPath = selectedCoverUri.toString()
            )
        }
    }

    open fun createPlaylist(onCompleted: () -> Unit) {

        val state = _createPlaylistState.value ?: return

        viewModelScope.launch {
            val savePath = selectedCoverUri?.let {
                playlistInteractor.saveImageToPrivateStorage(it)
            }
            playlistInteractor.createPlaylist(
                Playlist(
                    name = state.name,
                    description = state.description,
                    pathImageFile = savePath
                )
            )
            onCompleted()
        }
    }

    protected fun updateState(block: (CreatePlaylistState) -> CreatePlaylistState) {
        val current = _createPlaylistState.value ?: CreatePlaylistState()
        _createPlaylistState.value = block(current)
    }
}