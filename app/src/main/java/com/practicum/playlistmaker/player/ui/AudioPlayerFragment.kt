package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.player.PlayerState
import com.practicum.playlistmaker.player.domain.model.PlayerModel
import com.practicum.playlistmaker.player.domain.model.TrackToPlayerModelMapper
import com.practicum.playlistmaker.search.domain.entity.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {

    private lateinit var binding: FragmentAudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel()

    private var trackJson: String? = null

    companion object {
        private const val ARGS_TRACK_JSON = "track_json"

        fun newInstance(trackJson: String): AudioPlayerFragment {
            return AudioPlayerFragment().apply {
                arguments = bundleOf(ARGS_TRACK_JSON to trackJson)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackJson = requireArguments().getString(ARGS_TRACK_JSON)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val json = trackJson
        if (json.isNullOrEmpty()) {
            parentFragmentManager.popBackStack()
            return
        }

        val track = Gson().fromJson(json, Track::class.java)
        if (savedInstanceState == null) {
            viewModel.loadTrack(track)
        }

        viewModel.playerState.observe(viewLifecycleOwner) { state ->
            renderPlayerState(state)
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.backButton.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
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
                binding.timer.text = requireContext().getString(R.string.timer)
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
                binding.timer.text = requireContext().getString(R.string.timer_start)
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

        Glide.with(this).load(track.artworkUrl).placeholder(R.drawable.placeholder_cover)
            .fitCenter().transform(RoundedCorners(8)).into(binding.imageCover)
    }
}
