package com.example.soymusicreviewapp.ui.screens.songdetail

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SongsDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SongDetailState())

    val uiState: StateFlow<SongDetailState> = _uiState.asStateFlow()

    // Function to load song and reviews data based on the provided ID
    fun loadData(songId: Int) {
        // Find the song by ID
        val song = LocalSongsProvider.songs.find { it.songId == songId }
        val reviews = LocalReviewProvider.reviews

        _uiState.update { currentState ->
            currentState.copy(
                selectedSong = song,
                reviews = reviews
            )
        }
    }
}