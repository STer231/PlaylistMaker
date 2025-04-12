package com.practicum.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.themeSwitcher.isChecked = (applicationContext as App).isDarkTheme()

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        binding.share.setOnClickListener {
            val linkPracticum = getString(R.string.link_to_the_practicum)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, linkPracticum)
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        binding.support.setOnClickListener {
            val message = getString(R.string.support_message)
            val messageSubject = getString(R.string.support_subject)
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, messageSubject)
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(supportIntent)
        }

        binding.userAgreement.setOnClickListener {
            val linkOffer = getString(R.string.link_to_the_offer)
            val userAgreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(linkOffer))
            startActivity(userAgreementIntent)
        }
    }
}