package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object PreferencesKeysState {
        const val PREFERENCES_STATE = "player_state"
        const val KEY_PLAYER_STATE = "is_player_screen_active"
    }

    private lateinit var track: Track

    private lateinit var imageCover: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var trackDuration: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var countryTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val toolbar = findViewById<Toolbar>(R.id.backButton)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        trackTitle = findViewById(R.id.tv_TrackTitle)
        artistName = findViewById(R.id.tv_ArtistName)
        trackDuration = findViewById(R.id.tv_TrackDuration)
        collectionName = findViewById(R.id.tv_CollectionName)
        releaseDate = findViewById(R.id.tv_ReleaseDate)
        primaryGenreName = findViewById(R.id.tv_PrimaryGenreName)
        countryTitle = findViewById(R.id.tv_Country)
        imageCover = findViewById(R.id.iv_Cover)

        val trackJson = intent.getStringExtra("track_json")
        if (!trackJson.isNullOrEmpty()) {
            track = Gson().fromJson(trackJson, Track::class.java)
        } else {
            finish()
        }
        trackTitle.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        collectionName.text = track.collectionName
        releaseDate.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(track.releaseDate)
        primaryGenreName.text = track.primaryGenreName
        countryTitle.text = track.country
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_cover)
            .fitCenter()
            .transform(RoundedCorners(8))
            .into(imageCover)
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getSharedPreferences(PREFERENCES_STATE, MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean(KEY_PLAYER_STATE, true)
            .apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences(PREFERENCES_STATE, MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean(KEY_PLAYER_STATE, false)
            .apply()
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences(PREFERENCES_STATE, MODE_PRIVATE)
        val isPlayerScreenActive = sharedPreferences.getBoolean("isPlayerScreenActive", false)

        if (isPlayerScreenActive) {
            val intentAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
            startActivity(intentAudioPlayer)
        }
    }
}

fun Track.getCoverArtwork(): String {
    return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}