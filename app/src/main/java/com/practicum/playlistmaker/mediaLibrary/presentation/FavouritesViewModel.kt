package com.practicum.playlistmaker.mediaLibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.util.ErrorMessageProvider

class  FavouritesViewModel(
    private val errorMessageProvider: ErrorMessageProvider
) : ViewModel() {

    private val favouritesState = MutableLiveData<FavouritesState>()
    fun observeFavouritesState(): LiveData<FavouritesState> = favouritesState

    init {
        favouritesState.postValue(FavouritesState.Error(errorMessageProvider.emptyFavourites()))
    }
}