package com.example.soymusicreviewapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
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
}
