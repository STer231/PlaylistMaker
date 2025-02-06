package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

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

        trackTitle = findViewById(R.id.tv_TrackTitle)
        artistName = findViewById(R.id.tv_ArtistName)
        trackDuration = findViewById(R.id.tv_TrackDuration)
        collectionName = findViewById(R.id.tv_CollectionName)
        releaseDate = findViewById(R.id.tv_ReleaseDate)
        primaryGenreName = findViewById(R.id.tv_PrimaryGenreName)
        countryTitle = findViewById(R.id.tv_Country)
        imageCover = findViewById(R.id.iv_Cover)
//        Glide.with(this)
//            .load(track.artworkUrl100)
//            .placeholder(R.drawable.placeholder_cover)
//            .fitCenter()
//            .transform(RoundedCorners(2))
//            .into(imageCover)

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
    }
}