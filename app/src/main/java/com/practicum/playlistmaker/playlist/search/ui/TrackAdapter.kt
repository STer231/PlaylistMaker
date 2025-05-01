package com.practicum.playlistmaker.playlist.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.playlist.search.domain.entity.Track

class TrackAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    private val tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(ItemTrackBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(track)
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

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}

