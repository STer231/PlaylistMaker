package com.practicum.playlistmaker.player.data.impl

import com.practicum.playlistmaker.player.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.player.data.db.FavouriteTrackDao
import com.practicum.playlistmaker.player.data.repository.FavouriteRepository
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteRepositoryImpl(
    private val favouriteTrackDao: FavouriteTrackDao,
    private val trackDbConvertor: TrackDbConvertor,
) : FavouriteRepository {
    override suspend fun addToFavourite(track: Track) {
        favouriteTrackDao.insertTrack(trackDbConvertor.mapToEntity(track))
    }

    override suspend fun deleteFromFavourite(track: Track) {
        favouriteTrackDao.deleteTrack(trackDbConvertor.mapToEntity(track))
    }

    override fun getFavouritesTrack(): Flow<List<Track>> {
        return favouriteTrackDao.getTracks().map { entityList ->
            entityList.map { trackEntity -> trackDbConvertor.mapToDomain(trackEntity) }
        }
    }

    override suspend fun getFavouriteId(): List<Int> {
        return favouriteTrackDao.getTracksId()
    }
}