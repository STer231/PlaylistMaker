package com.practicum.playlistmaker.mediaLibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.search.ui.TrackViewHolder

class PlaylistDetailsTrackAdapter(
    private val onClick: (Track) -> Unit,
    private val onLongClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    private val tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(ItemTrackBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { onClick(track) }
        holder.itemView.setOnLongClickListener {
            onLongClick(track)
            true
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateData(newTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }
}