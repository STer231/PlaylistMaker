package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.mediaLibrary.presentation.PlaylistsState
import com.practicum.playlistmaker.mediaLibrary.presentation.PlaylistsViewModel
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

    private var adapter = PlaylistAdapter()

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

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlaylistFragment
            )
        }

        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(requireContext(),2,)
        binding.playlistsRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(message: String) {
        binding.apply {
            playlistsRecyclerView.visibility = View.GONE
            playlistsPlaceholderLayout.visibility = View.VISIBLE
            playlistsPlaceholderImage.setImageResource(R.drawable.placeholder_not_found)
            playlistsPlaceholderMessage.text = message
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        with(binding) {
            playlistsPlaceholderLayout.visibility = View.GONE
            playlistsRecyclerView.visibility = View.VISIBLE
        }
        adapter.updateData(playlists)
    }

    private fun renderState(state: PlaylistsState) {
        when(state) {
            is PlaylistsState.Error -> showError(state.message)
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }
}