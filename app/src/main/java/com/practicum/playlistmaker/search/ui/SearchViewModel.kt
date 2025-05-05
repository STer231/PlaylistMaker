package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.SearchState
import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractor

class SearchViewModel (application: Application) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(this[APPLICATION_KEY] as Application)
                }
            }
    }

    private val searchInteractor = Creator.provideSearchTracksInteractor(getApplication())

    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor(getApplication())

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
                                errorMessage = getApplication<Application>()
                                    .getString(R.string.problems_with_the_internet)
                            )
                        )
                    }
                    foundTracks.isNullOrEmpty() -> {
                        renderState(
                            SearchState.Empty(
                                message = getApplication<Application>()
                                    .getString(R.string.nothing_found)
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