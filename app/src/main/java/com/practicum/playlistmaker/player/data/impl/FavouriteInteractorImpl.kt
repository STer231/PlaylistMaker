package com.practicum.playlistmaker.player.data.impl

import com.practicum.playlistmaker.player.data.repository.FavouriteInteractor
import com.practicum.playlistmaker.player.data.repository.FavouriteRepository
import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteInteractorImpl(
    private val favouriteRepository: FavouriteRepository
) : FavouriteInteractor {
    override suspend fun addToFavourite(track: Track) {
        favouriteRepository.addToFavourite(track)
    }

    override suspend fun deleteFromFavourite(track: Track) {
        favouriteRepository.deleteFromFavourite(track)
    }

    override fun getFavouritesTrack(): Flow<List<Track>> {
        return favouriteRepository.getFavouritesTrack().map { list ->
            list.sortedByDescending { it.trackId }
        }
    }
}