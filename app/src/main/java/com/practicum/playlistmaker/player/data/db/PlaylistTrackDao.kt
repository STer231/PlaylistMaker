package com.practicum.playlistmaker.player.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(playlistTrackEntity: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks")
    fun getAllTracks(): Flow<List<PlaylistTrackEntity>>
}