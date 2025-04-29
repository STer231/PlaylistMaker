package com.practicum.playlistmaker.playlist.sharing.domain

import com.practicum.playlistmaker.playlist.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareText(text: String)
    fun openLink(url: String)
    fun sendEmail(emailData: EmailData)
}