package com.practicum.playlistmaker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(version = 1, entities = [FavouriteTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
      ALTER TABLE favourite_tracks 
      ADD COLUMN addedAt INTEGER NOT NULL DEFAULT 0
    """.trimIndent())
            }
        }
    }
    abstract fun favouriteTrackDao(): FavouriteTrackDao
}