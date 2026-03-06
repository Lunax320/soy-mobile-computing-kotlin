package com.example.soymusicreviewapp.ui.screens.reviewdetail

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ReviewDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewDetailState())

    val uiState: StateFlow<ReviewDetailState> = _uiState.asStateFlow()

    // Function to load a specific review by its ID
    fun loadReview(reviewId: Int) {
        val foundReview = LocalReviewProvider.reviews.find { it.usernameId == reviewId }
        val allReviews = LocalReviewProvider.reviews

        _uiState.update { currentState ->
            currentState.copy(
                selectedReview = foundReview,
                responseReviews = allReviews
            )
        }
    }
}