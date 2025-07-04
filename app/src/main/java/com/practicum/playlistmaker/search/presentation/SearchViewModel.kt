package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractor
import com.practicum.playlistmaker.util.ErrorMessageProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val errorMessageProvider: ErrorMessageProvider
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val _state = MutableLiveData<SearchState>(SearchState.Initial)
    val state: LiveData<SearchState> = _state

    private val _history = MutableLiveData<List<Track>>(emptyList())
    val history: LiveData<List<Track>> = _history

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    fun loadHistory() {
        val list = searchHistoryInteractor.getHistory()
        _history.postValue(list)
    }

    fun addToHistory(track: Track) {
        searchHistoryInteractor.addToHistory(track)
        loadHistory()
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        loadHistory()
    }

    fun clearSearch() {
        searchJob?.cancel()
        latestSearchText = null
        _state.postValue(SearchState.Initial)
    }

    fun retrySearch() {
        latestSearchText?.let { searchRequest(it) }
    }

    fun searchDebounce(changedText: String) {
        if (changedText == latestSearchText) return
        latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(query: String) {
        if (query.isBlank()) return
        _state.postValue(SearchState.Loading)

        searchInteractor.search(query, object : SearchTracksInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                when {
                    errorMessage != null -> {
                        val displayMessage =
                            if (errorMessage == "Проверьте подключение к интернету") {
                                errorMessageProvider.noInternet()
                            } else {
                                errorMessageProvider.serverError()
                            }
                        renderState(SearchState.Error(displayMessage))
                    }

                    foundTracks.isNullOrEmpty() -> {
                        renderState(
                            SearchState.Empty(
                                message = errorMessageProvider.nothingFound()
                            )
                        )
                    }
                    else -> {
                        renderState(SearchState.Content(foundTracks))
                    }
                }
            }
        })
    }

    private fun renderState(state: SearchState) {
        _state.postValue(state)
    }
}