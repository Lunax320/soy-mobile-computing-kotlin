package com.example.soymusicreviewapp.ui.screens.createreview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.repository.AuthRepository
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
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val songRepository: SongRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val songId: String = savedStateHandle.get<String>("songId") ?: ""

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    init {
        loadSongDetails()
    }

    private fun loadSongDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = songRepository.getSongById(songId)
                result.onSuccess { song ->
                    _uiState.update { it.copy(song = song) }

                    val userId = authRepository.currentUser?.uid ?: ""
                    if (userId.isNotEmpty()) {
                        val favResult = songRepository.isFavorite(userId, songId)
                        favResult.onSuccess { isFav ->
                            _uiState.update { it.copy(isFavorite = isFav) }
                        }
                    }
                    _uiState.update { it.copy(isLoading = false) }
                }.onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun onReviewTextChanged(text: String) {
        if (text.length <= 500) {
            _uiState.update { it.copy(reviewText = text) }
        }
    }

    fun onRatingChanged(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _uiState.update { it.copy(latitude = latitude, longitude = longitude) }
    }

    fun onFavoriteClick() {
        val userId = authRepository.currentUser?.uid ?: return
        val currentIsFav = _uiState.value.isFavorite
        
        viewModelScope.launch {
            val result = if (currentIsFav) {
                songRepository.removeFavorite(userId, songId)
            } else {
                songRepository.addFavorite(userId, songId)
            }
            
            result.onSuccess {
                _uiState.update { it.copy(isFavorite = !currentIsFav) }
            }.onFailure { e ->
                Log.e("CreateReviewVM", "Error al cambiar favorito: ${e.message}")
            }
        }
    }

    fun saveReview() {
        val currentState = _uiState.value
        val currentUser = authRepository.currentUser

        if (currentUser == null || currentState.song == null) {
            _uiState.update { it.copy(errorMessage = "Error: Faltan datos para crear la review") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val newReview = CreateReviewDto(
                    userId = currentUser.uid,
                    songId = currentState.song.songId,
                    songName = currentState.song.name,
                    artistName = currentState.song.artist,
                    reviewText = currentState.reviewText,
                    rating = currentState.rating,
                    date = System.currentTimeMillis().toString(),
                    parentId = null,
                    user = null,
                    latitude = currentState.latitude,
                    longitude = currentState.longitude
                )

                reviewRepository.createReview(newReview)

                _uiState.update { it.copy(isLoading = false, navigateBack = true) }
            } catch (e: Exception) {
                Log.e("CreateReviewVM", "Error guardando: ${e.message}")
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}
