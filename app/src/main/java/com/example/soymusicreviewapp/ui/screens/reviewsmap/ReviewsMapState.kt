package com.example.soymusicreviewapp.ui.screens.reviewsmap

import com.example.soymusicreviewapp.data.Review

data class ReviewsMapState(
    val isLoading: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val errorMessage: String? = null
)