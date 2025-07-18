package com.practicum.playlistmaker.player.data.impl

import com.practicum.playlistmaker.player.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.player.data.db.AppDatabase
import com.practicum.playlistmaker.player.data.db.FavouriteTrackEntity
import com.practicum.playlistmaker.player.data.repository.FavouriteRepository
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavouriteRepository {
    override suspend fun addToFavourite(track: Track) {
        appDatabase.favouriteTrackDao().insertTrack(trackDbConvertor.mapToEntity(track))
    }

    override suspend fun deleteFromFavourite(track: Track) {
        appDatabase.favouriteTrackDao().deleteTrack(trackDbConvertor.mapToEntity(track))
    }

    override fun getFavouritesTrack(): Flow<List<Track>> = flow{
        val tracks = appDatabase.favouriteTrackDao().getTracks()
        emit(convertersFromTrackEntity(tracks))
    }

    private fun convertersFromTrackEntity(tracks: List<FavouriteTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.mapToDomain(track) }
    }
}