package com.practicum.playlistmaker.mediaLibrary.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity): Long

    @Query("Select * FROM playlists")
    fun getPlaylists(): Flow<List<PlaylistEntity>>
}