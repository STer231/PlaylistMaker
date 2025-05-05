package com.practicum.playlistmaker.search.ui

import android.content.Context
import com.practicum.playlistmaker.R

class ErrorMessageProviderImpl(private val context: Context): ErrorMessageProvider {
    override fun noInternet(): String {
        return context.getString(R.string.problems_with_the_internet)
    }

    override fun nothingFound(): String {
        return context.getString(R.string.nothing_found)
    }

}