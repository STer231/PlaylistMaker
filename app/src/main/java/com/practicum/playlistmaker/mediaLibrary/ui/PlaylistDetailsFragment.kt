package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.mediaLibrary.presentation.PlaylistDetailsState
import com.practicum.playlistmaker.mediaLibrary.presentation.PlaylistDetailsViewModel
import com.practicum.playlistmaker.mediaLibrary.presentation.BottomSheetUiEvent
import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.util.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding: FragmentPlaylistDetailsBinding
        get() = _binding!!

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private val args: PlaylistDetailsFragmentArgs by navArgs()

    private val viewModel: PlaylistDetailsViewModel by viewModel()

    private lateinit var tracksAdapter: PlaylistDetailsTrackAdapter

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private lateinit var tracksBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var moreBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val currentPlaylist = args.playlistId
        viewModel.loadPlaylist(currentPlaylist)

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            val action = PlaylistDetailsFragmentDirections.actionPlaylistDetailsFragmentToAudioPlayerFragment(track)
            findNavController().navigate(action)
        }

        tracksAdapter = PlaylistDetailsTrackAdapter(
            onClick = { track ->
                onTrackClickDebounce(track)
            },
            onLongClick = { track ->
                showDeleteTrackDialog(track)
            }
        )

        binding.playlistDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistDetailsRecyclerView.adapter = tracksAdapter

        viewModel.playlistDetailsState.observe(viewLifecycleOwner) { state ->
            renderPlaylistDetails(state)
            renderTracks(state)
        }

        setupBottomSheetTracks()

        setupBottomSheetMore()

        binding.icMore.setOnClickListener {
            binding.overlay.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .start()
            }
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bottomSheetMoreEvents.collect { event ->
                    when(event) {
                        is BottomSheetUiEvent.NoTracksToShare -> {
                            Toast.makeText(requireContext(), R.string.no_tracks_to_share, Toast.LENGTH_SHORT).show()
                        }
                        is BottomSheetUiEvent.PlaylistDeleted -> {
                            if (findNavController().currentDestination?.id == R.id.playlistDetailsFragment) {
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }

        binding.icShare.setOnClickListener {
            viewModel.onShareCliched()
        }
        binding.btShareBottomSheet.setOnClickListener {
            viewModel.onShareCliched()
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        }
        binding.btDeletePlaylistBottomSheet.setOnClickListener {
            showDeletePlaylistDialog()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheetTracks() {
        tracksBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainerTracks).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        tracksBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) { }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        })
    }

    private fun renderPlaylistDetails(state: PlaylistDetailsState) {
        val playlist = state.playlist ?: return

        binding.tvPlaylistName.text = playlist.name
        if (!playlist.description.isNullOrBlank()) {
            binding.tvPlaylistDescription.visibility = View.VISIBLE
            binding.tvPlaylistDescription.text = playlist.description
        } else {
            binding.tvPlaylistDescription.visibility = View.GONE
        }
        binding.tvPlaylistDuration.text = viewModel.formatMinutesFromMillis(state.durationTrackSum)
        binding.tvPlaylistSize.text = "${playlist.playlistSize} треков"

        Glide.with(this)
            .load(playlist.pathImageFile)
            .placeholder(R.drawable.placeholder_cover)
            .transform(CenterCrop())
            .into(binding.ivCover)

        binding.tvPlaylistNameBottomSheet.text = playlist.name
        binding.tvPlaylistSizeBottomSheet.text = "${playlist.playlistSize} треков"

        Glide.with(this)
            .load(playlist.pathImageFile)
            .placeholder(R.drawable.placeholder_cover)
            .transform(
                CenterCrop(),
                RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.cover_radius_2))
            )
            .into(binding.ivPlaylistCoverBottomSheet)
    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.delete_track_dialog_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.removeTrack(track.trackId)
            }
            .setNegativeButton(R.string.no) { _, _ ->}
            .show()
    }

    private fun showDeletePlaylistDialog() {
        val playlist = viewModel.playlistDetailsState.value?.playlist ?: return
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("${getString(R.string.delete_playlist_dialog_message)} ${playlist.name}?")
            .setPositiveButton(R.string.yes) {_, _ ->
                viewModel.deletePlaylist(playlist.id)
            }
            .setNegativeButton(R.string.no) { _, _ ->}
            .show()
    }

    private fun renderTracks(state: PlaylistDetailsState) {
        if (state.tracks.isNotEmpty()) {
            binding.playlistsPlaceholderMessage.visibility = View.GONE
            binding.playlistDetailsRecyclerView.visibility = View.VISIBLE
            tracksAdapter.updateData(state.tracks)
        } else {
            tracksAdapter.updateData(emptyList())
            binding.playlistsPlaceholderMessage.text = requireContext().getText(R.string.nothing_found)
            binding.playlistsPlaceholderMessage.visibility = View.VISIBLE
            binding.playlistDetailsRecyclerView.visibility = View.GONE
        }
    }

    private fun setupBottomSheetMore() {
        moreBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainerMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        moreBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.animate()
                            .alpha(0f)
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
}