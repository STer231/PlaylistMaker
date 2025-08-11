package com.practicum.playlistmaker.mediaLibrary.presentation

sealed class BottomSheetUiEvent {
    data object NoTracksToShare : BottomSheetUiEvent()
    data object PlaylistDeleted: BottomSheetUiEvent()
}