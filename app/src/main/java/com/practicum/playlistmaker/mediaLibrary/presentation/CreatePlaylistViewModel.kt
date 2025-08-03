package com.practicum.playlistmaker.mediaLibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.domain.repository.CreatePlaylistInteractor
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor
): ViewModel() {

    private val _playlistName = MutableLiveData<String>("")
    val playlistName: LiveData<String> = _playlistName

    private val _playlistDescription = MutableLiveData<String>("")
    val playlistDescription: LiveData<String> = _playlistDescription

    private val _playlistCoverPath = MutableLiveData<String?>(null)
    val playlistCoverPath: LiveData<String?> = _playlistCoverPath

    private val _canCreate = MutableLiveData<Boolean>(false)
    val canCreate: LiveData<Boolean> = _canCreate

    fun onNameChange(name: String) {
        _playlistName.value = name
        _canCreate.value = name.isNotBlank()
    }

    fun onDescriptionChange(description: String) {
        _playlistDescription.value = description
    }

    fun onCoverSelected(path: String) {
        _playlistCoverPath.value = path
    }

    fun createPlaylist(onCompleted: () -> Unit) {
        val name = _playlistName.value.orEmpty()
        if (name.isBlank()) return
        val description = _playlistDescription.value
        val playlistCoverPath = _playlistCoverPath.value

        viewModelScope.launch {
            val newId = createPlaylistInteractor.createPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    pathImageFile = playlistCoverPath
                )
            )
            onCompleted()
        }
    }
}