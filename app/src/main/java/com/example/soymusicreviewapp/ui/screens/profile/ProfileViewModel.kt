package com.example.soymusicreviewapp.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.dtos.toUserProfileInfo
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.StorageRepository
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
class ProfileViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    val currentUserId: String
        get() = authRepository.currentUser?.uid ?: ""

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, currentUserId = currentUserId) }

            val userResult = userRepository.getUserById(userId)
            if (userResult.isSuccess) {
                val userDto = userResult.getOrNull()
                if (userDto != null) {
                    _uiState.update { it.copy(user = userDto.toUserProfileInfo()) }
                }
            }

            reviewRepository.getUserReviewsLive(userId)
                .catch { e -> Log.e("ProfileViewModel", "Error loading live reviews: ${e.message}") }
                .collect { reviews ->
                    _uiState.update { state ->
                        val currentPhoto = state.user.profileImageUrl ?: ""
                        val patchedReviews = reviews.map { 
                            if (it.userId == userId) it.copy(profileImage = currentPhoto) else it 
                        }
                        
                        state.copy(
                            userReviews = patchedReviews,
                            reviewCount = patchedReviews.size,
                            isLoading = false
                        )
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

    fun followOrUnfollowUser(targetUserId: String) {
        val sessionUserId = currentUserId
        if (sessionUserId.isEmpty()) return

        viewModelScope.launch {
            val result = userRepository.followOrUnfollowUser(sessionUserId, targetUserId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    val isNowFollowing = !state.user.followed
                    state.copy(
                        user = state.user.copy(
                            followed = isNowFollowing,
                            followersCount = if (isNowFollowing) state.user.followersCount + 1 else state.user.followersCount - 1
                        )
                    )
                }
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId)
        }
    }

    fun uploadImageToFirebase(uri: Uri) {
        // Optimistic update: Mostrar la imagen localmente primero
        _uiState.update { state ->
            state.copy(user = state.user.copy(profileImageUrl = uri.toString()), isLoading = true)
        }

        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess) {
                val imageUrl = result.getOrNull()
                if (imageUrl != null) {
                    val updateResult = userRepository.updateProfileImage(currentUserId, imageUrl)
                    if (updateResult.isSuccess) {
                        _uiState.update { state ->
                            state.copy(user = state.user.copy(profileImageUrl = imageUrl), isLoading = false)
                        }
                        Log.d("ProfileViewModel", "Imagen actualizada con éxito: $imageUrl")
                    } else {
                        Log.e("ProfileViewModel", "Error actualizando URL en Firestore")
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Error al vincular la foto con el perfil") }
                    }
                }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                Log.e("ProfileViewModel", "Error al subir a Firebase Storage: $error")
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al subir imagen: $error") }
            }
        }
    }
}
