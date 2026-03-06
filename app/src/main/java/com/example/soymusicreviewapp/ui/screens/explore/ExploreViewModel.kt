package com.example.soymusicreviewapp.ui.screens.explore

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExploreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreState())

    val uiState: StateFlow<ExploreState> = _uiState.asStateFlow()

    init {
        loadSongs()
    }

    // Update search text state
    fun onSearchChange(newText: String) {
        _uiState.update { currentState ->
            currentState.copy(searchText = newText)
        }
    }

    // Load songs from provider into state
    private fun loadSongs() {
        val allSongs = LocalSongsProvider.songs
        _uiState.update { currentState ->
            currentState.copy(songs = allSongs)
        }
    }
}