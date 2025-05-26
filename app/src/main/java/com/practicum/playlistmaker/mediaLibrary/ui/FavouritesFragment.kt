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
import org.koin.core.parameter.parametersOf

class FavouritesFragment : Fragment() {

    companion object {
        private const val FAVOURITES_LIST = "favourites_list"

        fun newInstance() = FavouritesFragment().apply {
            arguments = bundleOf()
        }
    }

    private lateinit var binding: FragmentFavouritesBinding

    private val favouritesViewModel: FavouritesViewModel by viewModel {
        parametersOf(requireArguments().getString(FAVOURITES_LIST))
    }

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

        favouritesViewModel.observeFavouritesLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun showError(message: String) {
        binding.apply {
            placeholderLayout.visibility = View.VISIBLE
            placeholderImage.setImageResource(R.drawable.placeholder_not_found)
            placeholderMessage.text = message
        }
    }

    private fun renderState(state: FavouriteState) {
        when(state) {
            is FavouriteState.Error -> showError(state.message)
        }
    }
}