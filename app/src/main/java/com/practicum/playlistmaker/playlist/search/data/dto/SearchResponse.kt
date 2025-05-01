package com.practicum.playlistmaker.playlist.search.data.dto

import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: ArrayList<TrackDto>
) : Response()