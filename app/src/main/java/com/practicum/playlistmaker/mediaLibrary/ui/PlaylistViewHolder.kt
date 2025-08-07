package com.practicum.playlistmaker.mediaLibrary.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist

class PlaylistViewHolder(
    private val binding: ItemPlaylistBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        binding.tvPlaylistName.text = model.name
        binding.tvPlaylistSize.text = "${model.playlistSize} треков"
        Glide.with(itemView)
            .load(model.pathImageFile)
            .placeholder(R.drawable.placeholder_cover)
            .transform(CenterCrop(),RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.cover_radius_8)))
            .into(binding.ivPlaylistCover)
    }
}