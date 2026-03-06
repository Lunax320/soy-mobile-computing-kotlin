package com.example.soymusicreviewapp.ui.screens.latest

import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.Song

data class LatestFeedState(
    // List of songs for the New Releases
    val newReleases: List<Song> = emptyList(),
    val recentReviews: List<Review> = emptyList()
)