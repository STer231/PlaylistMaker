package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavouritesFragment().apply {
            arguments = bundleOf()
        }
    }

    private lateinit var binding: FragmentFavouritesBinding

    private val favouritesViewModel: FavouritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesViewModel.observeFavouritesState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun showError(message: String) {
        binding.apply {
            favouritePlaceholderLayout.visibility = View.VISIBLE
            favouritePlaceholderImage.setImageResource(R.drawable.placeholder_not_found)
            favouritePlaceholderMessage.text = message
        }
    }

    private fun renderState(state: FavouritesState) {
        when(state) {
            is FavouritesState.Error -> showError(state.message)
        }
    }
}