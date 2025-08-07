package com.practicum.playlistmaker.mediaLibrary.data.impl

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistCoverStorageRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PlaylistCoverStorageRepositoryImpl(
    private val context: Context
) : PlaylistCoverStorageRepository {
    override fun saveImageToPrivateStorage(uri: Uri): String? {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlist_cover"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "cover_${System.currentTimeMillis()}.jpg")

        return try {
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