package com.example.soymusicreviewapp.ui.screens.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestFeedViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LatestFeedState())
    val uiState: StateFlow<LatestFeedState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val allSongs = LocalSongsProvider.songs
        _uiState.update { currentState ->
            currentState.copy(newReleases = allSongs.take(3))
        }

        viewModelScope.launch {
            val reviewsResult = reviewRepository.getReviews()

            if (reviewsResult.isSuccess) {
                _uiState.update { currentState ->
                    currentState.copy(recentReviews = reviewsResult.getOrNull() ?: emptyList())
                }
            } else {
                android.util.Log.e("API_ERROR", "Error al descargar reseñas Latest: ${reviewsResult.exceptionOrNull()?.message}")
            }
        }
    }
}