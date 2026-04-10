package com.example.soymusicreviewapp.ui.screens.foryou

import com.example.soymusicreviewapp.data.Review

data class ForYouFeedState(
    // List of recommended reviews to display
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,    // <-- Ruedita de carga
    val errorMessage: String? = null   // <-- Mensaje de error
)