package com.practicum.playlistmaker.mediaLibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.mediaLibrary.domain.model.Playlist
import com.practicum.playlistmaker.player.ui.PlaylistBottomSheetAdapter.PlaylistClickListener

class PlaylistAdapter(
    private val clickListener: PlaylistClickListener
): RecyclerView.Adapter<PlaylistViewHolder>() {

    private val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ItemPlaylistBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(playlist)
        }
    }

    fun updateData(newPlaylist: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylist)
        notifyDataSetChanged()
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}