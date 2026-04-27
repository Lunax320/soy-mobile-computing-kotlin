package com.example.soymusicreviewapp.ui.screens.commentreview

import com.example.soymusicreviewapp.data.Review

data class CommentReviewState(
    val commentText: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val errorMessage: String? = null,
    val parentReview: Review? = null,
    val comments: List<Review> = emptyList(),
    val currentUserId: String = ""
)