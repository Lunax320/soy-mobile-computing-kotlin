package com.example.soymusicreviewapp.ui.screens.create

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateReviewViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CreateReviewState())

    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    init {
        loadSongs()
    }

    // Function to update search text state
    fun onSearchChange(newText: String) {
        _uiState.update { currentState ->
            currentState.copy(searchText = newText)
        }
    }

    // Load songs from LocalSongsProvider into state
    private fun loadSongs() {
        val allSongs = LocalSongsProvider.songs
        _uiState.update { currentState ->
            currentState.copy(songs = allSongs)
        }
    }
}