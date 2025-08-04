package com.practicum.playlistmaker.mediaLibrary.ui

import android.net.Uri
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
import com.practicum.playlistmaker.mediaLibrary.domain.repository.PlaylistCoverStorageRepository
import com.practicum.playlistmaker.mediaLibrary.presentation.CreatePlaylistViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding: FragmentCreatePlaylistBinding
        get() = _binding!!

    private var selectedCoverUri: Uri? = null

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModel()

    private val playlistCoverStorage: PlaylistCoverStorageRepository by inject()

    private val pickPhoto = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            selectedCoverUri = it
            Glide.with(this)
                .load(it)
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.cover_radius_8))
                )
                .into(binding.ivCover)
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

        createPlaylistViewModel.canCreate.observe(viewLifecycleOwner) { enabled ->
            binding.btnCreatePlaylist.isEnabled = enabled
        }

        binding.ivCover.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.edTitle.doAfterTextChanged { name ->
            createPlaylistViewModel.onNameChange(name?.toString().orEmpty())
        }

        binding.edDescription.doAfterTextChanged { description ->
            createPlaylistViewModel.onDescriptionChange(description?.toString().orEmpty())
        }

        binding.btnCreatePlaylist.setOnClickListener {
            if (!savePlaylistCover()) return@setOnClickListener
            createPlaylistViewModel.createPlaylist {
                findNavController().navigateUp()
            }
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Завершить") {_, _ ->
                findNavController().navigateUp()
            }
            .setNeutralButton("Отмена") { _, _ -> }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backNavigation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
         _binding = null
    }

    private fun hasUnsavedData(): Boolean {
        val isNameNotEmpty = binding.edTitle.text?.isNotBlank() == true
        val isDescriptionNotEmpty = binding.edDescription.text?.isNotBlank() == true
        val isCoverNotEmpty = selectedCoverUri != null
        return isNameNotEmpty || isDescriptionNotEmpty || isCoverNotEmpty
    }

    private fun backNavigation() {
        if (hasUnsavedData()) {
            confirmDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun savePlaylistCover():Boolean {
        selectedCoverUri?.let { uri ->
            val savePath = playlistCoverStorage.saveImageToPrivateStorage(uri)
            return if (savePath != null) {
                createPlaylistViewModel.onCoverSelected(savePath)
                true
            } else {
                Toast.makeText(requireContext(),
                    "Не удалось сохранить обложку", Toast.LENGTH_SHORT
                ).show()
                false
            }
        }
        return true
    }
}
