package com.example.soymusicreviewapp.ui.screens.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingFeedViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(FollowingFeedState())
    val uiState: StateFlow<FollowingFeedState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
        loadReviews()
    }

    private fun loadCurrentUser() {
        val userId = authRepository.currentUser?.uid ?: ""
        _uiState.update { it.copy(currentUserId = userId) }
    }

    private fun loadReviews() {
        viewModelScope.launch {
            reviewRepository.getReviewsLive()
                .catch { e -> 
                    android.util.Log.e("API_ERROR", "Error en tiempo real (Following): ${e.message}")
                }
                .collect { reviews ->
                    _uiState.update { it.copy(reviews = reviews) }
                }
        }
    }

    fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            reviewRepository.sendOrDeleteReviewLike(reviewId, userId)
        }
    }
}
