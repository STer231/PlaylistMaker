package com.practicum.playlistmaker.util

interface ErrorMessageProvider {
    fun serverError(): String
    fun nothingFound(): String
    fun noInternet(): String
    fun emptyFavourites(): String
    fun emptyPlaylists(): String
}