package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = bundleOf()
        }
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(message: String) {
        binding.apply {
            playlistsPlaceholderLayout.visibility = View.VISIBLE
            playlistsPlaceholderImage.setImageResource(R.drawable.placeholder_not_found)
            playlistsPlaceholderMessage.text = message
        }
    }

    private fun renderState(state: PlaylistsState) {
        when(state) {
            is PlaylistsState.Error -> showError(state.message)
        }
    }
}