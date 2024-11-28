package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity_DarkMode : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dark_mode)

        val searchButton = findViewById<Button>(R.id.search)
        val mediaButton = findViewById<Button>(R.id.media)
        val settingsButton = findViewById<Button>(R.id.settings)

        searchButton.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }

        mediaButton.setOnClickListener {
            val intentMedia = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intentMedia)
        }

        settingsButton.setOnClickListener {
            val intentSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentSettings)
        }
    }
}