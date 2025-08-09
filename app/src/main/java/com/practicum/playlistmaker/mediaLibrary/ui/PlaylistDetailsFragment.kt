package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.mediaLibrary.presentation.PlaylistDetailsState
import com.practicum.playlistmaker.mediaLibrary.presentation.PlaylistDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding: FragmentPlaylistDetailsBinding
        get() = _binding!!

    private val args: PlaylistDetailsFragmentArgs by navArgs()

    private val viewModel: PlaylistDetailsViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        viewModel.playlistDetailsState.observe(viewLifecycleOwner) { state ->
            renderPlaylistDetails(state)
        }

        setupBottomSheet()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) { }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        })
    }

    private fun renderPlaylistDetails(state: PlaylistDetailsState) {
        val playlist = state.playlist

        if (playlist == null) {
            Toast.makeText(requireContext(), getString(R.string.nothing_found), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

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
    }
}