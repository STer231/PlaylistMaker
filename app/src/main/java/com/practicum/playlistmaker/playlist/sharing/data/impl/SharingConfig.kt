package com.practicum.playlistmaker.playlist.sharing.data.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.sharing.domain.model.EmailData

class SharingConfig(private val context: Context) {
    val appLink: String
        get() = context.getString(R.string.link_to_the_practicum)

    val termsLink: String
        get() = context.getString(R.string.link_to_the_offer)

    val supportEmailData: EmailData
        get() = EmailData(
            receivers = arrayOf(context.getString(R.string.support_email)),
            subject = context.getString(R.string.support_subject),
            body = context.getString(R.string.support_message)
        )
}