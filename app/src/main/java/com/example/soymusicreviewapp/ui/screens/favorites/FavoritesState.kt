package com.example.soymusicreviewapp.ui.screens.favorites

import com.example.soymusicreviewapp.data.Song

data class FavoritesState(
    val favoriteSongs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUserId: String = ""
)