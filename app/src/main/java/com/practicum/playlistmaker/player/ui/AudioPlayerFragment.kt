package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.player.presentation.PlayerState
import com.practicum.playlistmaker.player.domain.model.PlayerModel
import com.practicum.playlistmaker.player.domain.model.TrackToPlayerModelMapper
import com.practicum.playlistmaker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmaker.search.domain.entity.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding: FragmentAudioPlayerBinding
        get() = _binding!!

    private val viewModel: AudioPlayerViewModel by viewModel()

    private val args: AudioPlayerFragmentArgs by navArgs()

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = args.trackArg
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            findNavController().navigateUp()
        }

        viewModel.isFavourite.observe(viewLifecycleOwner) { favourite ->
            renderFavouriteButton(favourite)
        }

        binding.btAddToFavourites.setOnClickListener {
            viewModel.onFavouriteClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun renderFavouriteButton(isFavourite: Boolean) {
        val icon = if (isFavourite) {
            R.drawable.ic_favourites
        } else {
            R.drawable.ic_not_favourites
        }
        binding.btAddToFavourites.setImageResource(icon)
    }
}
