package com.practicum.playlistmaker.playlist.sharing.domain.impl

import com.practicum.playlistmaker.playlist.sharing.data.impl.SharingConfig
import com.practicum.playlistmaker.playlist.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.playlist.sharing.domain.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val config: SharingConfig,
    ): SharingInteractor {

    override fun shareApp() {
        return externalNavigator.shareText(config.appLink)
    }

    override fun openTerms() {
        return externalNavigator.openLink(config.termsLink)
    }

    override fun openSupport() {
        return externalNavigator.sendEmail(config.supportEmailData)
    }
}