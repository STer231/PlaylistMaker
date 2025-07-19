package com.practicum.playlistmaker.mediaLibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.data.repository.FavouriteInteractor
import com.practicum.playlistmaker.util.ErrorMessageProvider
import kotlinx.coroutines.launch

class  FavouritesViewModel(
    private val favouriteInteractor: FavouriteInteractor,
    private val errorMessageProvider: ErrorMessageProvider
) : ViewModel() {

    private val favouritesState = MutableLiveData<FavouritesState>()
    fun observeFavouritesState(): LiveData<FavouritesState> = favouritesState

    init {
        viewModelScope.launch {
            favouriteInteractor.getFavouritesTrack()
                .collect { list ->
                    if (list.isEmpty()) {
                        favouritesState.postValue(FavouritesState.Error(errorMessageProvider.emptyFavourites()))
                    } else {
                        favouritesState.postValue(FavouritesState.Content(list))
                    }
                }
        }

    }
}