package com.practicum.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity
import com.practicum.playlistmaker.mediaLibrary.ui.MediaLibraryActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }

        binding.media.setOnClickListener {
            val intentMedia = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intentMedia)
        }

        binding.settings.setOnClickListener {
            val intentSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentSettings)
        }

    }
}