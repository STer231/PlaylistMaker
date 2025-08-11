package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediaLibrary.presentation.CreatePlaylistState
import com.practicum.playlistmaker.mediaLibrary.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment: CreatePlaylistFragment() {

    private val args: EditPlaylistFragmentArgs by navArgs()

    override val createPlaylistViewModel: EditPlaylistViewModel by viewModel { parametersOf(args.playlistId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.title = getString(R.string.edit)
        binding.btnCreatePlaylist.text = getString(R.string.save)

        binding.btnCreatePlaylist.setOnClickListener {
            createPlaylistViewModel.createPlaylist {
                findNavController().navigateUp()
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${createPlaylistViewModel.createPlaylistState.value?.name} изменён",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun renderState(state: CreatePlaylistState) {
        super.renderState(state)

        val coverPath = state.coverPath
        if (coverPath.isNullOrBlank()) {
            binding.ivCover.setImageResource(R.drawable.placeholder_cover)
        }
    }

    override fun hasUnsavedData(): Boolean {
        return false
    }
}