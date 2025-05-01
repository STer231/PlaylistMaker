package com.practicum.playlistmaker.playlist.search.data.network

import com.practicum.playlistmaker.playlist.search.data.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SearchResponse>
}