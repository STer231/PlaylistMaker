package com.practicum.playlistmaker.playlist.search.data.impl

import com.practicum.playlistmaker.playlist.creator.Resource
import com.practicum.playlistmaker.playlist.search.data.NetworkClient
import com.practicum.playlistmaker.playlist.search.data.dto.SearchResponse
import com.practicum.playlistmaker.playlist.search.data.dto.SearchTracksRequest
import com.practicum.playlistmaker.playlist.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.playlist.search.domain.entity.Track
import com.practicum.playlistmaker.playlist.search.domain.repository.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchTracksRequest(expression))
        return when (response.resultCode) {
            // Оставил логику с "-1" на случай, если потребуется по-разному реагировать на отсутствие интернета и проблему с сервером
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as SearchResponse).results.map {
                    TrackMapper.mapToDomain(it) })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}