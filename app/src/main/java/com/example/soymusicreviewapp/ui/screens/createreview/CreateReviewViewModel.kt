package com.example.soymusicreviewapp.ui.screens.createreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.soymusicreviewapp.data.repository.ReviewRepository

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    fun onReviewTextChange(newText: String) {
        if (newText.length <= 500) {
            _uiState.update { currentState ->
                currentState.copy(reviewText = newText)
            }
        }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { currentState ->
            currentState.copy(rating = newRating)
        }
    }

    fun createReview(songId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = reviewRepository.createReview(
                songId = songId,
                reviewText = _uiState.value.reviewText,
                rating = _uiState.value.rating
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(navigateBack = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al publicar") }
            }
        }
    }

    fun getSong(songId: String): Song? {
        return LocalSongsProvider.songs.find { it.songId == songId }
    }
}