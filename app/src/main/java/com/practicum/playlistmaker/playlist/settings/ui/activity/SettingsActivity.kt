package com.practicum.playlistmaker.playlist.settings.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.playlist.creator.Creator
import com.practicum.playlistmaker.playlist.settings.ui.viewModel.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels {
        val settingsInteractor = Creator.provideSettingsInteractor(this)
        val sharingInteractor = Creator.provideSharingInteractor(this)
        SettingsViewModel.getViewModelFactory(settingsInteractor, sharingInteractor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.observeState().observe(this) { state ->
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



//        binding.share.setOnClickListener {
//            val linkPracticum = getString(R.string.link_to_the_practicum)
//            val shareIntent = Intent(Intent.ACTION_SEND).apply {
//                type = "text/plain"
//                putExtra(Intent.EXTRA_TEXT, linkPracticum)
//            }
//            startActivity(Intent.createChooser(shareIntent, null))
//        }
//
//        binding.support.setOnClickListener {
//            val message = getString(R.string.support_message)
//            val messageSubject = getString(R.string.support_subject)
//            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
//                data = Uri.parse("mailto:")
//                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
//                putExtra(Intent.EXTRA_SUBJECT, messageSubject)
//                putExtra(Intent.EXTRA_TEXT, message)
//            }
//            startActivity(supportIntent)
//        }
//
//        binding.userAgreement.setOnClickListener {
//            val linkOffer = getString(R.string.link_to_the_offer)
//            val userAgreementIntent =
//                Intent(Intent.ACTION_VIEW, Uri.parse(linkOffer))
//            startActivity(userAgreementIntent)
//        }
    }
}