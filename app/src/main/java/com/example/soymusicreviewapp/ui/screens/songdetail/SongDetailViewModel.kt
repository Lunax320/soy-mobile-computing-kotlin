package com.example.soymusicreviewapp.ui.screens.songdetail

import android.util.Log
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
import javax.inject.Inject

@HiltViewModel
class SongsDetailViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SongDetailState())
    val uiState: StateFlow<SongDetailState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val userId = authRepository.currentUser?.uid ?: ""
        _uiState.update { it.copy(currentUserId = userId) }
    }

    fun loadData(songId: String) {
        viewModelScope.launch {
            songRepository.getSongsLive()
                .catch { e -> Log.e("SongsDetailViewModel", "Error loading song live: ${e.message}") }
                .collect { allSongs ->
                    val updatedSong = allSongs.find { it.songId == songId }
                    updatedSong?.let { song ->
                        _uiState.update { it.copy(selectedSong = song) }
                    }
                }
        }

        viewModelScope.launch {
            reviewRepository.getSongReviewsLive(songId)
                .catch { e -> Log.e("SongsDetailViewModel", "Error loading reviews live: ${e.message}") }
                .collect { songReviews ->
                    _uiState.update { it.copy(reviews = songReviews) }
                }
        }
    }

    fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            reviewRepository.sendOrDeleteReviewLike(reviewId, userId)
        }
    }
}
