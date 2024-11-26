package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Переходим в меню поиск", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        buttonSearch.setOnClickListener(buttonClickListener)

        val buttonMedia = findViewById<Button>(R.id.media)
        buttonMedia.setOnClickListener {
            Toast.makeText(this@MainActivity, "Переходим в мультимедиа", Toast.LENGTH_SHORT).show()
        }
        val buttonSettings = findViewById<Button>(R.id.settings)
        buttonSettings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Переходим в настройки", Toast.LENGTH_SHORT).show()
        }
    }
}