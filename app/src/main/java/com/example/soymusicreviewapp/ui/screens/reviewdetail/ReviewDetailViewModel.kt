package com.example.soymusicreviewapp.ui.screens.reviewdetail

import android.util.Log
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
class ReviewDetailViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ReviewDetailState())
    val uiState: StateFlow<ReviewDetailState> = _uiState.asStateFlow()

    fun loadReview(reviewId: String) {
        viewModelScope.launch {
            val result = reviewRepository.getReviewById(reviewId)

            if (result.isSuccess) {
                val review = result.getOrNull()

                val commentsResult = reviewRepository.getCommentsForReview(reviewId)
                val comments = commentsResult.getOrNull() ?: emptyList()

                _uiState.update { currentState ->
                    currentState.copy(
                        selectedReview = review,
                        responseReviews = comments
                    )
                }
            } else {
                Log.e("API_TRACKER", "Reseña - no se pudo cargar la review con id: $reviewId")
            }
        }
    }
}