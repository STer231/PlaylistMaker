package com.practicum.playlistmaker.playlist.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.playlist.search.SearchState
import com.practicum.playlistmaker.ui.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var historyAdapter: TrackAdapter

    private var isClickAllowed = true

    private val clickHandler = Handler(Looper.getMainLooper())

    private lateinit var adapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        adapter = TrackAdapter(TrackAdapter.TrackClickListener { track ->
            if (clickDebounce()) {
                viewModel.addToHistory(track)
                val intentAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
                val trackJson = Gson().toJson(track)
                intentAudioPlayer.putExtra("track_json", trackJson)
                startActivity(intentAudioPlayer)
            }
        })

        historyAdapter = TrackAdapter { track ->
            viewModel.addToHistory(track)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistory.adapter = historyAdapter

        viewModel.state.observe(this) { state ->
            renderState(state)
        }

        viewModel.history.observe(this) { historyList ->
            historyAdapter.updateData(historyList)
            binding.historyLayout.visibility =
                if (historyList.isEmpty()) View.GONE else View.VISIBLE
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {
            binding.inputEditText.setText("")
            binding.inputEditText.clearFocus()
            hideKeyboard()
            viewModel.clearSearch()
        }

        binding.refreshButton.setOnClickListener {
            viewModel.retrySearch()
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty()) {
                viewModel.loadHistory()
            }
            updateHistoryVisibility()
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString().orEmpty().trim()
                binding.clearButton.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
                viewModel.searchDebounce(text)
                updateHistoryVisibility()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateHistoryVisibility() {
        val historyList = viewModel.history.value.orEmpty()
        binding.historyLayout.visibility =
            if (binding.inputEditText.hasFocus() && binding.inputEditText.text.isEmpty() && historyList.isNotEmpty())
                View.VISIBLE else View.GONE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.Initial -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.placeholderLayout.visibility = View.GONE
            }

            is SearchState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.placeholderLayout.visibility = View.GONE
            }

            is SearchState.Content -> {
                binding.progressBar.visibility = View.GONE
                binding.placeholderLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.updateData(state.tracks)
            }

            is SearchState.Empty -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.placeholderLayout.visibility = View.VISIBLE
                binding.errorPlaceholderImage.setImageResource(R.drawable.placeholder_not_found)
                binding.placeholderText.text = state.message
                binding.refreshButton.visibility = View.GONE
            }

            is SearchState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.placeholderLayout.visibility = View.VISIBLE
                binding.errorPlaceholderImage.setImageResource(R.drawable.placeholder_no_internet)
                binding.placeholderText.text = state.errorMessage
                binding.refreshButton.visibility = View.VISIBLE
            }
        }
    }

    private fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
    }
}