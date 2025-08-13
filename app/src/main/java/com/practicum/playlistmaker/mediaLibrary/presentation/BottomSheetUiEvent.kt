package com.practicum.playlistmaker.mediaLibrary.presentation

sealed interface BottomSheetUiEvent {
    data object NoTracksToShare : BottomSheetUiEvent
    data object PlaylistDeleted: BottomSheetUiEvent
}