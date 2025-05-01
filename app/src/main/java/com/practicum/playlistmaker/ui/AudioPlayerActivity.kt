package com.practicum.playlistmaker.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.search.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val PREFERENCES_STATE = "player_state"
        const val KEY_PLAYER_STATE = "is_player_screen_active"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TIMER_DELAY = 300L
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
    private lateinit var playButton: ImageButton
    private lateinit var timer: TextView
    private lateinit var url: String

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                val currentTime = mediaPlayer.currentPosition
                val formattedTime = dateFormat.format(currentTime)
                timer.text = formattedTime
                mainThreadHandler.postDelayed(this, TIMER_DELAY)
            }
        }
    }

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
        playButton = findViewById(R.id.bt_play)
        timer = findViewById(R.id.tv_Timer)

        val trackJson = intent.getStringExtra("track_json")
        if (!trackJson.isNullOrEmpty()) {
            track = Gson().fromJson(trackJson, Track::class.java)
            url = track.previewUrl
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

        preparePlayer()
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getSharedPreferences(PREFERENCES_STATE, MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean(KEY_PLAYER_STATE, true)
            .apply()

        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences(PREFERENCES_STATE, MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean(KEY_PLAYER_STATE, false)
            .apply()

        mediaPlayer.release()
        mainThreadHandler.removeCallbacks(updateTimerRunnable)
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

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play)
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(updateTimerRunnable)
            timer.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
        mainThreadHandler.post(updateTimerRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
        mainThreadHandler.removeCallbacks(updateTimerRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}

fun Track.getCoverArtwork(): String {
    return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}