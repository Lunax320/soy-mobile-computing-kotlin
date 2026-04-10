package com.example.soymusicreviewapp.ui.screens.songdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsDetailViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SongDetailState())
    val uiState: StateFlow<SongDetailState> = _uiState.asStateFlow()

    fun loadData(songId: String) {
        viewModelScope.launch {
            val songResult = songRepository.getSongById(songId)
            val song = songResult.getOrNull()

            val reviewResult = reviewRepository.getReviews()

            if (reviewResult.isSuccess && song != null) {
                val allReviews = reviewResult.getOrNull() ?: emptyList()
                val songReviews = allReviews.filter { it.songId == songId || it.songName == song.name }

                _uiState.update { currentState ->
                    currentState.copy(
                        selectedSong = song,
                        reviews = songReviews
                    )
                }
            }
        }
    }
}