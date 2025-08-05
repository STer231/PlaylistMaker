package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.presentation.PlaylistsState
import com.practicum.playlistmaker.player.presentation.PlayerState
import com.practicum.playlistmaker.player.domain.model.PlayerModel
import com.practicum.playlistmaker.player.domain.model.TrackToPlayerModelMapper
import com.practicum.playlistmaker.player.presentation.AddTrackResultState
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

    private lateinit var adapter: PlaylistBottomSheetAdapter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        setupBottomSheet()

        binding.btAddToPlaylist.setOnClickListener {
            binding.overlay.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_audioPlayerFragment_to_createPlaylistFragment
            )
        }

        viewModel.addTrackToPlaylistResult.observe(viewLifecycleOwner) { result ->
            handleAddTrackToPlaylistResult(result)
        }

        adapter =
            PlaylistBottomSheetAdapter(PlaylistBottomSheetAdapter.PlaylistClickListener { palylist ->
                viewModel.checkAddResult(palylist)
            })

        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistsRecyclerView.adapter = adapter

        viewModel.playlistsState.observe(viewLifecycleOwner) {
            renderPlaylistsState(it)
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

        Glide.with(this)
            .load(track.artworkUrl)
            .placeholder(R.drawable.placeholder_cover)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.cover_radius_8))
            )
            .into(binding.imageCover)
    }

    private fun renderFavouriteButton(isFavourite: Boolean) {
        val icon = if (isFavourite) {
            R.drawable.ic_favourites
        } else {
            R.drawable.ic_not_favourites
        }
        binding.btAddToFavourites.setImageResource(icon)
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.animate()
                            .alpha(0f)
                            .setDuration(150)
                            .withEndAction { binding.overlay.visibility = View.GONE }
                            .start()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = 1f + slideOffset
                if (binding.overlay.alpha <= 0f) {
                    binding.overlay.visibility = View.GONE
                }
            }
        })
    }

    private fun renderPlaylistsState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Error -> showError(state.message)
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }

    private fun showError(message: String) {
        binding.apply {
            playlistsRecyclerView.visibility = View.GONE
            playlistsPlaceholderMessage.visibility = View.VISIBLE
            playlistsPlaceholderMessage.text = message
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        with(binding) {
            playlistsPlaceholderMessage.visibility = View.GONE
            playlistsRecyclerView.visibility = View.VISIBLE
        }
        adapter.updateData(playlists)
    }

    private fun handleAddTrackToPlaylistResult(result: AddTrackResultState) {
        when(result) {
            is AddTrackResultState.Added -> {
                showTrackAddedToast(result.playlistName)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            is AddTrackResultState.AlreadyHas -> {
                showTrackAlreadyAddedToast(result.playlistName)
            }
        }

    }

    private fun showTrackAddedToast(playlistName: String) {
        Toast.makeText(
            requireContext(),
            "${requireContext().getString(R.string.track_added_playlist)} $playlistName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showTrackAlreadyAddedToast(playlistName: String) {
        Toast.makeText(
            requireContext(),
            "${requireContext().getString(R.string.track_already_in_playlist)} $playlistName",
            Toast.LENGTH_LONG
        ).show()
    }
}
