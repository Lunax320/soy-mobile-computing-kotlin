package com.example.soymusicreviewapp.ui.screens.createreview

data class CreateReviewState(
    val reviewText: String = "",
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigateBack: Boolean = false
)