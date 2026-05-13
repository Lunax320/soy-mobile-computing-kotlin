package com.example.soymusicreviewapp.ui.screens.reviewsmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsMapViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewsMapState())
    val uiState: StateFlow<ReviewsMapState> = _uiState.asStateFlow()

    init {
        loadReviewsForMap()
    }

    private fun loadReviewsForMap() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val reviews = reviewRepository.getReviewsForMap()
                _uiState.update { it.copy(reviews = reviews, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}