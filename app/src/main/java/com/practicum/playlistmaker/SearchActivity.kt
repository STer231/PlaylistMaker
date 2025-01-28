package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerViev: RecyclerView
    private lateinit var placeholderLayout: ViewGroup
    private lateinit var placeholderText: TextView
    private lateinit var refreshButton: Button
    private lateinit var errorPlaceholder: ImageView

    private var searchText: String? = null
    private var lastQuery: String? = null

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    val trackList = ArrayList<Track>()
    val adapter = TrackAdapter(trackList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearIcon)
        recyclerViev = findViewById(R.id.recyclerView)
        placeholderLayout = findViewById(R.id.placeholderLayout)
        placeholderText = findViewById(R.id.tv_error_placeholder_text)
        refreshButton = findViewById(R.id.bt_error_placeholder_refresh_request)
        errorPlaceholder = findViewById(R.id.iv_error_placeholder)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            placeholderLayout.visibility = View.GONE
            trackList.clear()
            adapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s?.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString("search_text")
            inputEditText.setText(searchText)
        }

        recyclerViev.layoutManager = LinearLayoutManager(this)
        recyclerViev.adapter = adapter

        refreshButton.setOnClickListener {
            lastQuery?.let { query ->
                searchTracks(query)
            }
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = inputEditText.text.toString()
                if (query.isNotEmpty()) {
                    searchTracks(query)
                }
                true
            }
            false
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("search_text", searchText)
        outState.putString("last_query", lastQuery)
        val gson = Gson()
        val trackListJson = gson.toJson(trackList)
        outState.putString("track_list", trackListJson)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("search_text")
        inputEditText.setText(searchText)
        val gson = Gson()
        val trackListJson = savedInstanceState.getString("track_list")
        if (!trackListJson.isNullOrEmpty()) {
            val restoredTrackList = gson.fromJson(trackListJson, Array<Track>::class.java)
            trackList.clear()
            trackList.addAll(restoredTrackList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun searchTracks(query: String) {
        lastQuery = query
        trackList.clear()
        adapter.notifyDataSetChanged()
        showRecyclerView()
        itunesService.search(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    if (!response.body()?.results.isNullOrEmpty()) {
                        trackList.clear()
                        trackList.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                    } else {
                        showMessage(
                            getString(R.string.nothing_found),
                            false,
                            R.drawable.placeholder_not_found
                        )
                    }
                } else {
                    showMessage(
                        getString(R.string.problems_with_the_internet),
                        true,
                        R.drawable.placeholder_no_internet,
                    )
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showMessage(
                    getString(R.string.problems_with_the_internet),
                    true,
                    R.drawable.placeholder_no_internet,
                )
            }
        })
    }

    private fun showRecyclerView() {
        placeholderLayout.visibility = View.GONE
        recyclerViev.visibility = View.VISIBLE
    }

    private fun showMessage(
        text: String,
        isNetworkProblem: Boolean = false,
        placeholder: Int,
    ) {
        placeholderLayout.visibility = View.VISIBLE
        recyclerViev.visibility = View.GONE
        placeholderText.text = text
        errorPlaceholder.setImageResource(placeholder)
        refreshButton.visibility = if (isNetworkProblem) View.VISIBLE else View.GONE

    }
}