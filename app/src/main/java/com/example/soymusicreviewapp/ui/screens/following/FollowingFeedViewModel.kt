package com.example.soymusicreviewapp.ui.screens.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.UserRepository
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
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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
            val currentUserId = _uiState.value.currentUserId
            if (currentUserId.isEmpty()) return@launch

            val followingIds = userRepository.getFollowingIds(currentUserId)

            reviewRepository.getReviewsLive()
                .catch { e -> 
                    android.util.Log.e("FollowingVM", "Error en feed social: ${e.message}")
                }
                .collect { allReviews ->
                    val filteredReviews = allReviews.filter { it.userId in followingIds }
                    
                    _uiState.update { it.copy(reviews = filteredReviews) }
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
