package com.practicum.playlistmaker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [FavouriteTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteTrackDao(): FavouriteTrackDao
}