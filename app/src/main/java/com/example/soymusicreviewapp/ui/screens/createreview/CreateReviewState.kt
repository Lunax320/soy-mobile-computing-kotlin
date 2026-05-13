package com.example.soymusicreviewapp.ui.screens.createreview

import com.example.soymusicreviewapp.data.Song

data class CreateReviewState(
    val reviewText: String = "",
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigateBack: Boolean = false,
    val song: Song? = null,
    val isFavorite: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null
)