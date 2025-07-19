package com.practicum.playlistmaker.player.data.repository

import com.practicum.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    suspend fun addToFavourite(track: Track)

    suspend fun deleteFromFavourite(track: Track)

    fun getFavouritesTrack(): Flow<List<Track>>
}