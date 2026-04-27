package com.example.soymusicreviewapp.ui.screens.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestFeedViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    private val songRepository: SongRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LatestFeedState())
    val uiState: StateFlow<LatestFeedState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
        loadData()
    }

    private fun loadCurrentUser() {
        val userId = authRepository.currentUser?.uid ?: ""
        _uiState.update { it.copy(currentUserId = userId) }
    }

    private fun loadData() {
        viewModelScope.launch {
            songRepository.getSongsLive()
                .catch { e ->
                    android.util.Log.e("LatestViewModel", "Error loading songs live: ${e.message}")
                }
                .collect { songs ->
                    _uiState.update { currentState ->
                        currentState.copy(newReleases = songs.take(3))
                    }
                }
        }

        viewModelScope.launch {
            reviewRepository.getMainReviewsLive()
                .catch { e ->
                    android.util.Log.e("API_ERROR", "Error en tiempo real (Latest): ${e.message}")
                }
                .collect { reviews ->
                    _uiState.update { currentState ->
                        currentState.copy(recentReviews = reviews)
                    }
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
