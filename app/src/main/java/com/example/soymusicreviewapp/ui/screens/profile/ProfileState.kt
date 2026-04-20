package com.example.soymusicreviewapp.ui.screens.profile

import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.UserProfileInfo

data class ProfileState(
    val user: UserProfileInfo = UserProfileInfo(),
    val userReviews: List<Review> = emptyList(),
    val reviewCount: Int = 0,
    val currentUserId: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
