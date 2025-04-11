package com.practicum.playlistmaker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
) {
    private val ivCover: ImageView = itemView.findViewById(R.id.ivCover)
    private val tvTrackTitle: TextView = itemView.findViewById(R.id.tv_TrackTitle)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tv_ArtistName)
    private val tvTrackDuration: TextView = itemView.findViewById(R.id.tv_TrackDuration)

    fun bind(model: Track) {
        tvTrackTitle.text = model.trackName
        tvArtistName.text = model.artistName
        tvArtistName.requestLayout()
        tvTrackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)
        println(model.artworkUrl100)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_cover)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(ivCover)
    }
}