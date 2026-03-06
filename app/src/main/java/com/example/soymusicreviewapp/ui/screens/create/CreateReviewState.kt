package com.example.soymusicreviewapp.ui.screens.create

import com.example.soymusicreviewapp.data.Song
data class CreateReviewState(
    // Stores the text input from SearchBar
    val searchText: String = "",
    val songs: List<Song> = emptyList()
)