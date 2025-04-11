package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.SearchResponse
import com.practicum.playlistmaker.data.dto.SearchTracksRequest
import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.domain.repository.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(SearchTracksRequest(expression))
        return if (response.resultCode == 200) {
            (response as SearchResponse).results.map { TrackMapper.mapToDomain(it) }
        } else {
            emptyList()
        }
    }
}