package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("trackId") val trackId: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String
)
