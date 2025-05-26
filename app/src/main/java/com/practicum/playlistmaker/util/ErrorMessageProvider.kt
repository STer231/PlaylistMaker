package com.practicum.playlistmaker.util

interface ErrorMessageProvider {
    fun serverError(): String
    fun nothingFound(): String
    fun emptyFavourites(): String
    fun emptyPlaylist(): String
}