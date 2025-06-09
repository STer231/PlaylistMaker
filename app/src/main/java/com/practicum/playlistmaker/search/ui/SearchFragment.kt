package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.SearchState
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.search.domain.entity.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var historyAdapter: TrackAdapter

    private val viewModel: SearchViewModel by viewModel()

    private var isClickAllowed = true

    private val clickHandler = Handler(Looper.getMainLooper())

    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(TrackAdapter.TrackClickListener { track ->
            if (clickDebounce()) {
                viewModel.addToHistory(track)
                val trackJson = Gson().toJson(track)
                parentFragmentManager.commit {
                    replace(
                        R.id.fragmentContainerView,
                        AudioPlayerFragment.newInstance(trackJson)
                    )
                    addToBackStack(null)
                }
            }
        })

        historyAdapter = TrackAdapter { track ->
            viewModel.addToHistory(track)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHistory.adapter = historyAdapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewModel.history.observe(viewLifecycleOwner) { historyList ->
            historyAdapter.updateData(historyList)
            binding.historyLayout.visibility =
                if (historyList.isEmpty()) View.GONE else View.VISIBLE
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
            is SearchState.Initial -> showInitial()
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty(state.message)
            is SearchState.Error -> showError(state.errorMessage)
        }
    }

    private fun showInitial() {
        hideAll()
    }

    private fun showLoading() {
        hideAll()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showContent(tracks: List<Track>) {
        with(binding) {
            progressBar.visibility = View.GONE
            placeholderLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        adapter.updateData(tracks)
    }

    private fun showEmpty(message: String) {
        with(binding) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            placeholderLayout.visibility = View.VISIBLE
            errorPlaceholderImage.setImageResource(R.drawable.placeholder_not_found)
            placeholderText.text = message
            refreshButton.visibility = View.GONE
        }
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            placeholderLayout.visibility = View.VISIBLE
            errorPlaceholderImage.setImageResource(R.drawable.placeholder_no_internet)
            placeholderText.text = errorMessage
            refreshButton.visibility = View.VISIBLE
        }
    }

    private fun hideAll() {
        with(binding) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            placeholderLayout.visibility = View.GONE
        }
    }

    private fun hideKeyboard() {
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
    }
}