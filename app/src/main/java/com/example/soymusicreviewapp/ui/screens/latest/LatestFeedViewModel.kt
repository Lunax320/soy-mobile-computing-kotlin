package com.example.soymusicreviewapp.ui.screens.latest

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LatestFeedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LatestFeedState())

    val uiState: StateFlow<LatestFeedState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    // Loads both songs and reviews from providers
    private fun loadData() {
        val allSongs = LocalSongsProvider.songs
        val allReviews = LocalReviewProvider.reviews

        _uiState.update { currentState ->
            currentState.copy(
                // Logic to take only the first 3 songs for new releases
                newReleases = allSongs.take(3),
                recentReviews = allReviews
            )
        }
    }
}