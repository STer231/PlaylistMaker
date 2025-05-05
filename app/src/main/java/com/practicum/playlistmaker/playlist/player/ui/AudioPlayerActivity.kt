package com.practicum.playlistmaker.playlist.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.playlist.player.domain.model.PlayerModel
import com.practicum.playlistmaker.playlist.player.domain.model.TrackToPlayerModelMapper
import com.practicum.playlistmaker.playlist.search.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding

    companion object {
        private const val EXTRA_TRACK_JSON = "track_json"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]

        val trackJson = intent.getStringExtra(EXTRA_TRACK_JSON)
        if (!trackJson.isNullOrEmpty()) {
            val track = Gson().fromJson(trackJson, Track::class.java)
            if (savedInstanceState == null) {
                viewModel.loadTrack(track)
            }
        } else {
            finish()
            return
        }

        viewModel.playerState.observe(this) { state ->
            renderPlayerState(state)
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.backButton.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun renderPlayerState(state: PlayerState) {
        when (state) {

            is PlayerState.Ready -> {
                renderTrack(state.track)
                binding.playButton.setImageResource(R.drawable.ic_play)
                binding.playButton.isEnabled = true
                binding.timer.text = getText(R.string.timer)
            }

            is PlayerState.Playing -> {
                renderTrack(state.track)
                binding.playButton.setImageResource(R.drawable.ic_pause)
                binding.playButton.isEnabled = true
                binding.timer.text = TrackToPlayerModelMapper.mapDuration(state.currentPosition)
            }

            is PlayerState.Paused -> {
                renderTrack(state.track)
                binding.playButton.setImageResource(R.drawable.ic_play)
                binding.timer.text = TrackToPlayerModelMapper.mapDuration(state.currentPosition)
            }

            is PlayerState.Completed -> {
                renderTrack(state.track)
                binding.playButton.setImageResource(R.drawable.ic_play)
                binding.timer.text = getText(R.string.timer_start)
            }
        }
    }

    private fun renderTrack(track: PlayerModel) {
        binding.trackTitle.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackDuration.text = track.formattedDuration
        binding.collectionName.text = track.collectionName
        binding.releaseDate.text = track.formattedReleaseYear
        binding.primaryGenreName.text = track.primaryGenreName
        binding.countryName.text = track.country

        Glide.with(this)
            .load(track.artworkUrl)
            .placeholder(R.drawable.placeholder_cover)
            .fitCenter()
            .transform(RoundedCorners(8))
            .into(binding.imageCover)
    }
}
