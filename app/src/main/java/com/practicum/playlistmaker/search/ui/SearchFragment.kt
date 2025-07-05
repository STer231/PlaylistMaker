package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.search.presentation.SearchState
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.search.domain.entity.Track
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var historyAdapter: TrackAdapter

    private val viewModel: SearchViewModel by viewModel()

    private var adapter: TrackAdapter? = null

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            viewModel.addToHistory(track)
            val trackJson = Gson().toJson(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(trackJson)
            )
        }

        adapter = TrackAdapter(TrackAdapter.TrackClickListener { track ->
            (activity as RootActivity).animateBottomNavigationView()
            onTrackClickDebounce(track)
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
            updateHistoryVisibility()
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
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString().orEmpty().trim()
                binding.clearButton.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
                if (text.isEmpty()) {
                    viewModel.clearSearch()
                    adapter?.updateData(emptyList())
                } else {
                    viewModel.searchDebounce(text)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

    private fun updateHistoryVisibility() {
        val historyList = viewModel.history.value.orEmpty()
        val isInitialState = viewModel.state.value is SearchState.Initial
        val isInputEmpty = binding.inputEditText.text.isEmpty()
        val hasFocus = binding.inputEditText.hasFocus()

        val shouldShow = isInitialState && hasFocus && isInputEmpty && historyList.isNotEmpty()
        binding.historyLayout.visibility = if (shouldShow) View.VISIBLE else View.GONE
    }

    private fun renderState(state: SearchState) {
        hideAll()
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
        updateHistoryVisibility()
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
        adapter?.updateData(tracks)
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
            binding.historyLayout.visibility = View.GONE
        }
    }

    private fun hideKeyboard() {
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
    }
}