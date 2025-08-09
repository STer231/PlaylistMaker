package com.practicum.playlistmaker.mediaLibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.mediaLibrary.presentation.CreatePlaylistState
import com.practicum.playlistmaker.mediaLibrary.presentation.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding: FragmentCreatePlaylistBinding
        get() = _binding!!

    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModel()

    private val pickPhoto = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            createPlaylistViewModel.onCoverSelected(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setNavigationOnClickListener {
            backNavigation()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backNavigation()
        }

        binding.ivCover.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        createPlaylistViewModel.createPlaylistState.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        binding.edTitle.doAfterTextChanged { name ->
            createPlaylistViewModel.onNameChange(name?.toString().orEmpty())
        }

        binding.edDescription.doAfterTextChanged { description ->
            createPlaylistViewModel.onDescriptionChange(description?.toString().orEmpty())
        }

        binding.btnCreatePlaylist.setOnClickListener {
            createPlaylistViewModel.createPlaylist {
                findNavController().navigateUp()
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${createPlaylistViewModel.createPlaylistState.value?.name} создан",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hasUnsavedData(): Boolean {
        val state = createPlaylistViewModel.createPlaylistState.value ?: return false
        return state.name.isNotBlank() || state.description.isNotBlank() || state.coverPath != null
    }

    private fun backNavigation() {
        if (hasUnsavedData()) {
            confirmExitDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun renderState(state: CreatePlaylistState) {
        binding.btnCreatePlaylist.isEnabled = state.canCreate

        if (binding.edTitle.text.toString() != state.name) {
            binding.edTitle.setText(state.name)
        }
        if (binding.edDescription.text.toString() != state.description) {
            binding.edDescription.setText(state.description)
        }

        if (state.coverPath != null) {
            Glide.with(this)
                .load(state.coverPath)
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.cover_radius_8))
                )
                .into(binding.ivCover)
        }
    }

    private fun confirmExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_dialog_title)
            .setMessage(R.string.confirm_dialog_message)
            .setNegativeButton(R.string.complete) { _, _ ->
                findNavController().navigateUp()
            }
            .setNeutralButton(R.string.cansel) { _, _ -> }
            .show()
    }
}
