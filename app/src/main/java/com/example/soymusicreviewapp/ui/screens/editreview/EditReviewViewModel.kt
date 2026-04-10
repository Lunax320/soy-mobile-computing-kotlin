package com.example.soymusicreviewapp.ui.screens.editreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import kotlinx.coroutines.launch

@HiltViewModel
class EditReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditReviewState())
    val uiState: StateFlow<EditReviewState> = _uiState.asStateFlow()

    // Maneja el texto y las estrellas (ya lo tenías)
    fun onReviewTextChange(newText: String) {
        if (newText.length <= 500) {
            _uiState.update { it.copy(reviewText = newText) }
        }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { it.copy(rating = newRating) }
    }

    // LA FUNCIÓN CLAVE: Guardar los cambios (Punto 1.5)
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
                // navigateBack es un flag que tu Screen debe observar para cerrar la pantalla
                _uiState.update { it.copy(isLoading = false, navigateBack = true) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun getSong(songId: String): Song? {
        return LocalSongsProvider.songs.find { it.songId == songId }
    }
}