package com.practicum.playlistmaker.util

import android.content.Context

class PluralsProviderImpl(private val context: Context): PluralsProvider {
    override fun getQuantityString(resId: Int, quantity: Int, vararg formatArgs: Any): String {
        return context.applicationContext.resources.getQuantityString(resId, quantity, *formatArgs)
    }
}