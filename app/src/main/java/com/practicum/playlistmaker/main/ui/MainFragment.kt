package com.practicum.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.search.ui.SearchFragment
import com.practicum.playlistmaker.settings.ui.SettingsFragment
import com.practicum.playlistmaker.mediaLibrary.ui.MediaLibraryFragment


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, SearchFragment())
                addToBackStack(null)
            }
        }

        binding.media.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, MediaLibraryFragment())
                addToBackStack(null)
            }
        }

        binding.settings.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, SettingsFragment())
                addToBackStack(null)
            }
        }
    }
}