package com.example.soymusicreviewapp.ui.screens.profile

import com.example.soymusicreviewapp.data.Review

data class ProfileState(
    val profileImageId: Int = 0,
    val name: String = "",
    val username: String = "",
    val reviewCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val userReviews: List<Review> = emptyList()
)
