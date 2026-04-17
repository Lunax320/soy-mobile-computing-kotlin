package com.example.soymusicreviewapp.ui.screens.editreview

import com.example.soymusicreviewapp.data.Song

data class EditReviewState(
    val selectedSong: Song? = null,
    val reviewText: String = "",
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val navigateBack: Boolean = false
)