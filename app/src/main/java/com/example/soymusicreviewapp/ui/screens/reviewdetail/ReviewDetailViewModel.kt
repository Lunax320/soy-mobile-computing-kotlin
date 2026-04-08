package com.example.soymusicreviewapp.ui.screens.reviewdetail

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ReviewDetailViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(ReviewDetailState())

    val uiState: StateFlow<ReviewDetailState> = _uiState.asStateFlow()

    // Function to load a specific review by its ID
    fun loadReview(reviewId: String) {
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