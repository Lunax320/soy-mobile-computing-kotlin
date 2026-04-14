package com.example.soymusicreviewapp.ui.screens.createreview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val songRepository: SongRepository, // Se inyecta el repositorio de canciones
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    init {
        val songId = savedStateHandle.get<String>("songId") ?: ""
        if (songId.isNotEmpty()) {
            loadSongData(songId)
        }
    }

    private fun loadSongData(songId: String) {
        viewModelScope.launch {
            val result = songRepository.getSongById(songId)

            if (result.isSuccess) {
                val fetchedSong = result.getOrNull()
                if (fetchedSong != null) {
                    _uiState.update { it.copy(song = fetchedSong) }
                }
            } else {
                Log.e("API_TRACKER", "Crear Reseña: Fallo al descargar la información de la canción. Motivo: ${result.exceptionOrNull()?.message}")
            }
        }
    }

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

    fun createReview(songId: String) {
        Log.d("API_TRACKER", "ViewModel: Iniciando publicación para la canción: $songId")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

            val result = reviewRepository.createReview(
                userId = currentUserId,
                songId = songId,
                reviewText = _uiState.value.reviewText,
                rating = _uiState.value.rating,
                date = currentDate
            )

            if (result.isSuccess) {
                Log.d("API_TRACKER", "ViewModel: Publicación exitosa.")
                _uiState.update { it.copy(navigateBack = true) }
            } else {
                Log.e("API_TRACKER", "ViewModel: Fallo en la publicación.")
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al publicar") }
            }
        }
    }
}