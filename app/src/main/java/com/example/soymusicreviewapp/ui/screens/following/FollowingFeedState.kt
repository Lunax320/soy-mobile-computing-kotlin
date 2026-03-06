package com.example.soymusicreviewapp.ui.screens.following

import com.example.soymusicreviewapp.data.Review

data class FollowingFeedState(
    val reviews: List<Review> = emptyList()
)