package com.practicum.playlistmaker.util

import android.content.Context
import com.practicum.playlistmaker.R

class ErrorMessageProviderImpl(private val context: Context): ErrorMessageProvider {
    override fun serverError(): String {
        return context.getString(R.string.problems_with_the_internet)
    }

    override fun nothingFound(): String {
        return context.getString(R.string.nothing_found)
    }

    override fun noInternet(): String {
        return context.getString(R.string.no_internet)
    }

    override fun emptyFavourites(): String {
       return context.getString(R.string.empty_favourites)
    }

    override fun emptyPlaylists(): String {
        return context.getString(R.string.empty_playlists)
    }
}