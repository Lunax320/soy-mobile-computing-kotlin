package com.example.soymusicreviewapp.ui.screens.foryou

import com.example.soymusicreviewapp.data.Review

data class ForYouFeedState(
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUserId: String = ""
)
