package com.practicum.playlistmaker.player.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: FavouriteTrackEntity)

    @Delete(entity = FavouriteTrackEntity::class)
    suspend fun deleteTrack(trackEntity: FavouriteTrackEntity)

    @Query("Select * FROM favourite_tracks ORDER BY lastAdded DESC")
    fun getTracks(): Flow<List<FavouriteTrackEntity>>

    @Query("Select trackId FROM favourite_tracks")
    suspend fun getTracksId(): List<Int>
}