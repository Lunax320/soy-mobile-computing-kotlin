package com.example.soymusicreviewapp.ui.screens.songdetail

import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.Song

data class SongDetailState(
    val selectedSong: Song? = null,
    val reviews: List<Review> = emptyList()
)