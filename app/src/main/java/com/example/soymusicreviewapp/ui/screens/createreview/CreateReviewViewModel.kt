package com.example.soymusicreviewapp.ui.screens.createreview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val songRepository: SongRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    private var currentSongId: String = ""

    init {
        val songId = savedStateHandle.get<String>("songId") ?: ""
        if (songId.isNotEmpty()) {
            currentSongId = songId
            loadSongData(songId)
            loadFavoriteStatus(songId)
        }
    }

    private fun loadSongData(songId: String) {
        viewModelScope.launch {
            songRepository.getSongsLive()
                .catch { e -> Log.e("API_TRACKER", "Error loading song: ${e.message}") }
                .collect { allSongs ->
                    val updatedSong = allSongs.find { it.songId == songId }
                    updatedSong?.let { song ->
                        _uiState.update { it.copy(song = song) }
                    }
                }
        }
    }

    private fun loadFavoriteStatus(songId: String) {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            val result = songRepository.isFavorite(userId, songId)
            if (result.isSuccess) {
                _uiState.update { it.copy(isFavorite = result.getOrNull() ?: false) }
            }
        }
    }

    fun onReviewTextChange(newText: String) {
        if (newText.length <= 500) {
            _uiState.update { it.copy(reviewText = newText) }
        }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { it.copy(rating = newRating) }
    }

    fun onFavoriteClick() {
        val userId = authRepository.currentUser?.uid ?: return
        val songId = currentSongId
        val isFavorite = _uiState.value.isFavorite

        _uiState.update { it.copy(isFavorite = !isFavorite) }

        viewModelScope.launch {
            val result = if (isFavorite) {
                songRepository.removeFavorite(userId, songId)
            } else {
                songRepository.addFavorite(userId, songId)
            }
            if (result.isFailure) {
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }

    fun createReview(songId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val currentUserId = authRepository.currentUser?.uid ?: ""

            val result = reviewRepository.createReview(
                userId = currentUserId,
                songId = songId,
                reviewText = _uiState.value.reviewText,
                rating = _uiState.value.rating,
                date = currentDate
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(navigateBack = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al publicar") }
            }
        }
    }
}