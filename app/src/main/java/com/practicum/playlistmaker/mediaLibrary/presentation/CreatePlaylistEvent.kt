package com.practicum.playlistmaker.mediaLibrary.presentation

open class CreatePlaylistEvent<out T>(private val content: T) {
    private var handled = false

    fun getContentIfNotHandled(): T? {
        return if(handled) {
            null
        } else {
            handled = true
            content
        }
    }
}