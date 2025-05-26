package com.practicum.playlistmaker.mediaLibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.util.ErrorMessageProvider

class FavouritesViewModel(
    private val errorMessageProvider: ErrorMessageProvider
) : ViewModel() {

    private val favouritesLiveData = MutableLiveData<FavouriteState>()
    fun observeFavouritesLiveData(): LiveData<FavouriteState> = favouritesLiveData

    init {
        favouritesLiveData.postValue(FavouriteState.Error(errorMessageProvider.emptyFavourites()))
    }
}