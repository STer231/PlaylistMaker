package com.practicum.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.search.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(
    private val binding: ItemTrackBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        binding.trackTitle.text = model.trackName
        binding.artistName.text = model.artistName
        binding.artistName.requestLayout()
        binding.trackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)
        println(model.artworkUrl100)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_cover)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(binding.ivCover)
    }
}