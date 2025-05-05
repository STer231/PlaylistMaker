package com.practicum.playlistmaker.search.ui

interface ErrorMessageProvider {
    fun noInternet(): String
    fun nothingFound(): String
}