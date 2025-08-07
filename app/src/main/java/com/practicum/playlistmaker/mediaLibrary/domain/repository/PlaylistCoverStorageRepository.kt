package com.practicum.playlistmaker.mediaLibrary.domain.repository

import android.net.Uri

interface PlaylistCoverStorageRepository {

    fun saveImageToPrivateStorage(uri: Uri): String?
}