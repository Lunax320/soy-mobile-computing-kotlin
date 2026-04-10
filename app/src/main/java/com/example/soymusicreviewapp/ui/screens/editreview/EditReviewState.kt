package com.example.soymusicreviewapp.ui.screens.editreview

data class EditReviewState(
    val reviewText: String = "",
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val navigateBack: Boolean = false
)