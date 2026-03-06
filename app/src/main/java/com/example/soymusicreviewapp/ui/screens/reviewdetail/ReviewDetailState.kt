package com.example.soymusicreviewapp.ui.screens.reviewdetail

import com.example.soymusicreviewapp.data.Review

data class ReviewDetailState(
    // The specific review selected
    val selectedReview: Review? = null,
    val responseReviews: List<Review> = emptyList()
)