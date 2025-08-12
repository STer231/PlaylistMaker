package com.practicum.playlistmaker.util

import androidx.annotation.PluralsRes

interface PluralsProvider {
    fun getQuantityString(@PluralsRes resId: Int,  quantity: Int, vararg formatArgs: Any): String
}