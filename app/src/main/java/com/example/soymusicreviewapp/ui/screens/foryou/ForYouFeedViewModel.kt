package com.example.soymusicreviewapp.ui.screens.foryou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.injection.IoDispatcher
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForYouFeedViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    private val _uiState = MutableStateFlow(ForYouFeedState())
    val uiState: StateFlow<ForYouFeedState> = _uiState.asStateFlow()

    init {
        loadReviews()
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val userId = authRepository.currentUser?.uid ?: ""
        _uiState.update { it.copy(currentUserId = userId) }
    }

    fun loadReviews() {
        viewModelScope.launch(ioDispatcher){
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            reviewRepository.getMainReviewsLive()
                .catch { e -> 
                    _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } 
                }
                .collect { reviews ->
                    _uiState.update { it.copy(reviews = reviews, isLoading = false, errorMessage = null) }
                }
        }
    }

    fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        if (userId.isEmpty()) return

        viewModelScope.launch(ioDispatcher) {
            val result = reviewRepository.sendOrDeleteReviewLike(reviewId, userId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    val updatedReviews = state.reviews.map { review ->
                        if (review.id == reviewId) {
                            val isCurrentlyLiked = review.liked
                            review.copy(
                                liked = !isCurrentlyLiked,
                                likesCount = if (isCurrentlyLiked) review.likesCount - 1 else review.likesCount + 1
                            )
                        } else {
                            review
                        }
                    }
                    state.copy(reviews = updatedReviews)
                }
            }
        }
    }
}
