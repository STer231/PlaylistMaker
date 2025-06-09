package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding

class RootActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                this.add(R.id.fragmentContainerView, MainFragment())
            }
        }
    }
}