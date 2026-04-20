package com.example.soymusicreviewapp.ui.screens.editreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.Song
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
class EditReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val songRepository: SongRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditReviewState())
    val uiState: StateFlow<EditReviewState> = _uiState.asStateFlow()

    fun onReviewTextChange(newText: String) {
        if (newText.length <= 500) {
            _uiState.update { it.copy(reviewText = newText) }
        }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { it.copy(rating = newRating) }
    }

    fun loadReviewData(reviewId: String, songId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val reviewResult = reviewRepository.getReviewById(reviewId)
            
            if (reviewResult.isSuccess) {
                val review = reviewResult.getOrNull()
                review?.let { r ->
                    _uiState.update { 
                        it.copy(
                            reviewText = r.reviewText,
                            rating = r.rating,
                            selectedSong = Song(
                                songId = r.songId,
                                songImage = "",
                                name = r.songName,
                                artist = r.artistName,
                                genre = "",
                                duration = ""
                            )
                        )
                    }
                }
            }

            val songResult = songRepository.getSongById(songId)
            if (songResult.isSuccess) {
                _uiState.update { it.copy(selectedSong = songResult.getOrNull(), isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun saveEdit(reviewId: String, songId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = reviewRepository.updateReview(
                reviewId = reviewId,
                songId = songId,
                reviewText = _uiState.value.reviewText,
                rating = _uiState.value.rating
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, navigateBack = true) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}