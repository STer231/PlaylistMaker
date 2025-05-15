package com.practicum.playlistmaker.player.domain.usecase

import androidx.lifecycle.LiveData
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepository
import com.practicum.playlistmaker.search.domain.entity.Track

class AudioPlayerInteractorImpl(
    private val repository: AudioPlayerRepository
) : AudioPlayerInteractor {

    override val onPrepared: LiveData<Unit> = repository.onPrepared
    override val onCompletion: LiveData<Unit> = repository.onCompletion

    override fun prepare(track: Track) {
        repository.prepare(track.previewUrl)
    }

    override fun startPlayback() = repository.start()

    override fun pausePlayback() = repository.pause()

    override fun releasePlayer() = repository.release()

    override fun getCurrentPosition(): Int = repository.currentPosition()
}
