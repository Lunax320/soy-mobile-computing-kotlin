package com.example.soymusicreviewapp.ui.screens.foryou

import com.example.soymusicreviewapp.data.Review

data class ForYouFeedState(
    // List of recommended reviews to display
    val reviews: List<Review> = emptyList()
)