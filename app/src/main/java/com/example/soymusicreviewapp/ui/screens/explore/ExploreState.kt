package com.example.soymusicreviewapp.ui.screens.explore

import com.example.soymusicreviewapp.data.Song

data class ExploreState(
    // Current text in the search bar
    val searchText: String = "",
    val songs: List<Song> = emptyList()
)