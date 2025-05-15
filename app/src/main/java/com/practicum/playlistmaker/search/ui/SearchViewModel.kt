package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.SearchState
import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractor

class SearchViewModel(
    private val searchInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val errorMessageProvider: ErrorMessageProvider
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val _state = MutableLiveData<SearchState>(SearchState.Initial)
    val state: LiveData<SearchState> = _state

    private val _history = MutableLiveData<List<Track>>(emptyList())
    val history: LiveData<List<Track>> = _history

    private val handler = Handler(Looper.getMainLooper())
    private var latestSearchText: String? = null


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
        _state.postValue(SearchState.Initial)
    }

    fun retrySearch() {
        latestSearchText?.let { searchRequest(it) }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        super.onCleared()
    }

    fun searchDebounce(changedText: String) {
        if (changedText == latestSearchText) return
        latestSearchText = changedText

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(query: String) {
        if (query.isBlank()) return
        _state.postValue(SearchState.Loading)

        searchInteractor.search(query, object : SearchTracksInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                when {
                    errorMessage != null -> {
                        renderState(
                            SearchState.Error(
                                errorMessage = errorMessageProvider.noInternet()
                            )
                        )
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