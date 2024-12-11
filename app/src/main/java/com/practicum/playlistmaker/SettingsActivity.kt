package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.appcompat.app.AppCompatActivity
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

        binding.share.setOnClickListener {
            val linkPracticum = getString(R.string.link_to_the_practicum)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, linkPracticum)
            startActivity(Intent.createChooser(shareIntent, "Отправить с помощью..."))
        }

        binding.support.setOnClickListener {
            val message = getString(R.string.support_message)
            val messageSubject = getString(R.string.support_subject)
            val supportEmail = getString(R.string.support_email)
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:$supportEmail")
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, messageSubject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
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