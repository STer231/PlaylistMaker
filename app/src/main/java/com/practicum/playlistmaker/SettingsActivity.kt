package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener {
            finish()
        }
    }
}