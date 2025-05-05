package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: ArrayList<TrackDto>
) : Response()