package com.example.soymusicreviewapp.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository,
    private val reviewRepository: ReviewRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    fun loadUserReviews(userId: String) {
        viewModelScope.launch {
            val result = reviewRepository.getReviews()
            if (result.isSuccess) {
                val allReviews = result.getOrNull() ?: emptyList()

                // FILTRO CORREGIDO: Compara los IDs de forma exacta
                val myReviews = allReviews.filter { it.userId == userId || it.usernameId == userId }
                val firstReview = myReviews.firstOrNull()

                _uiState.update { it.copy(
                    userReviews = myReviews,
                    reviewCount = myReviews.size,
                    name = firstReview?.userName ?: "Music Lover",
                    username = firstReview?.userName?.let { name -> "@${name.lowercase().replace(" ", "")}" } ?: "@musiclover",
                    profileImageUrl = firstReview?.profileImage ?: ""
                )}
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            val result = reviewRepository.deleteReview(reviewId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(userReviews = state.userReviews.filter { it.usernameId != reviewId })
                }
            }
        }
    }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess) {
                _uiState.update { it.copy(profileImageUrl = result.getOrNull()) }
            }
        }
    }
}