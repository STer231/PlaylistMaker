package com.practicum.playlistmaker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practicum.playlistmaker.mediaLibrary.data.db.PlaylistDao
import com.practicum.playlistmaker.mediaLibrary.data.db.PlaylistEntity

@Database(
    version = 3,
    entities = [
        FavouriteTrackEntity::class,
        PlaylistEntity::class
    ]
)

abstract class AppDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
      ALTER TABLE favourite_tracks 
      ADD COLUMN lastAdded INTEGER NOT NULL DEFAULT 0
    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS playlists (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT,
                    coverPath TEXT,
                    trackIdsJson TEXT NOT NULL,
                    trackCount INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent())
            }
        }
    }

    abstract fun favouriteTrackDao(): FavouriteTrackDao
    abstract fun playlistDao(): PlaylistDao
}