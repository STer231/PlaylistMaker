package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.mediaLibrary.presentation.FavouritesState
import com.practicum.playlistmaker.mediaLibrary.presentation.FavouritesViewModel
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavouritesFragment().apply {
            arguments = bundleOf()
        }

        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private var _binding: FragmentFavouritesBinding? = null
    private val binding: FragmentFavouritesBinding
        get() = _binding!!


    private val favouritesViewModel: FavouritesViewModel by viewModel()

    private var adapter: TrackAdapter? = null

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            val trackJson = Gson().toJson(track)
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(trackJson)
            )
        }

        adapter = TrackAdapter(TrackAdapter.TrackClickListener { track ->
            (activity as RootActivity).animateBottomNavigationView()
            onTrackClickDebounce(track)
        })

        binding.favouriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favouriteRecyclerView.adapter = adapter

        favouritesViewModel.observeFavouritesState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(message: String) {
        binding.apply {
            favouriteRecyclerView.visibility = View.GONE
            favouritePlaceholderLayout.visibility = View.VISIBLE
            favouritePlaceholderImage.setImageResource(R.drawable.placeholder_not_found)
            favouritePlaceholderMessage.text = message
        }
    }

    private fun showContent(tracks: List<Track>) {
        with(binding) {
            favouritePlaceholderLayout.visibility = View.GONE
            favouriteRecyclerView.visibility = View.VISIBLE
        }
        adapter?.updateData(tracks)
    }

    private fun renderState(state: FavouritesState) {
        when(state) {
            is FavouritesState.Error -> showError(state.message)
            is FavouritesState.Content -> showContent(state.favouriteTracks)
        }
    }
}