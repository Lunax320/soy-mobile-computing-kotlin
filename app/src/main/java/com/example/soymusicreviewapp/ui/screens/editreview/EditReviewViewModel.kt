package com.example.soymusicreviewapp.ui.screens.editreview

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditReviewViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EditReviewState())
    val uiState: StateFlow<EditReviewState> = _uiState.asStateFlow()

    fun onReviewTextChange(newText: String) {
        if (newText.length <= 500) {
            _uiState.update { currentState ->
                currentState.copy(reviewText = newText)
            }
        }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { currentState ->
            currentState.copy(rating = newRating)
        }
    }

    fun getSong(songId: String): Song? {
        return LocalSongsProvider.songs.find { it.songId == songId }
    }
}