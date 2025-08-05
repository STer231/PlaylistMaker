package com.practicum.playlistmaker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practicum.playlistmaker.mediaLibrary.data.db.PlaylistDao
import com.practicum.playlistmaker.mediaLibrary.data.db.PlaylistEntity

@Database(
    version = 1,
    entities = [
        FavouriteTrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class
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
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS playlist_tracks (
                        trackId INTEGER PRIMARY KEY NOT NULL,
                        artworkUrl TEXT NOT NULL,
                        trackName TEXT NOT NULL,
                        artistName TEXT NOT NULL,
                        collectionName TEXT,
                        releaseDate INTEGER NOT NULL,
                        primaryGenreName TEXT NOT NULL,
                        country TEXT NOT NULL,
                        trackTime INTEGER NOT NULL,
                        previewUrl TEXT NOT NULL
                        )
                        """.trimIndent()
                )
            }

        }
    }

    abstract fun favouriteTrackDao(): FavouriteTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
}