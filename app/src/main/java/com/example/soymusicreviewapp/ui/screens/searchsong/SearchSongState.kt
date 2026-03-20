package com.example.soymusicreviewapp.ui.screens.searchsong

import com.example.soymusicreviewapp.data.Song
data class SearchSongState(
    // Stores the text input from SearchBar
    val searchText: String = "",
    val songs: List<Song> = emptyList()
)