package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareText(text: String)
    fun openLink(url: String)
    fun sendEmail(emailData: EmailData)
}