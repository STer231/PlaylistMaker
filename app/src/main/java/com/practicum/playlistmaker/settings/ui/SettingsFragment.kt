package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            binding.themeSwitcher.isChecked = state.isDarkMode
            AppCompatDelegate.setDefaultNightMode(
                if (state.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.toggleTheme(checked)
        }

        binding.share.setOnClickListener {
            viewModel.shareApp()
        }

        binding.support.setOnClickListener {
            viewModel.contactSupport()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.openUserAgreement()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}