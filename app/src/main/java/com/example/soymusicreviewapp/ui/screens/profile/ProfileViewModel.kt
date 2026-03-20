package com.example.soymusicreviewapp.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.StorageRepository
import com.google.firebase.crashlytics.internal.common.Utils
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
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState(
        profileImageUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
    ))
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        // Mocking user profile loading.
        // In a real app, this would come from a Repository or a UserSession.
        _uiState.update { currentState ->
            currentState.copy(
                profileImageId = R.drawable.img_avatar_penguin,
                name = "Music Lover",
                username = "@musiclover",
                reviewCount = 2,
                followersCount = 234,
                followingCount = 189,
                userReviews = LocalReviewProvider.reviews // Assuming all reviews in LocalReviewProvider belong to the user for now
            )
        }
    }

    //fun updateProfileImageUrl(profileImageUrl: Uri) = _uiState.update { it.copy(profileImageUrl = profileImageUrl) }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(profileImageUrl = result.getOrNull())
                }
            }
        }
    }
}


