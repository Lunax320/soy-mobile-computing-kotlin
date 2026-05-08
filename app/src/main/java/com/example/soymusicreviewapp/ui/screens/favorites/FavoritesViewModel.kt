package com.example.soymusicreviewapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
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
class FavoritesViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesState())
    val uiState: StateFlow<FavoritesState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
        loadFavorites()
    }

    private fun loadCurrentUser() {
        val userId = authRepository.currentUser?.uid ?: ""
        _uiState.update { it.copy(currentUserId = userId) }
    }

    fun loadFavorites() {
        val userId = _uiState.value.currentUserId
        if (userId.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            songRepository.getFavoriteSongsLive(userId)
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message)
                    }
                }
                .collect { songs ->
                    _uiState.update {
                        it.copy(favoriteSongs = songs, isLoading = false, errorMessage = null)
                    }
                }
        }
    }

    fun removeFavorite(songId: String) {
        val userId = _uiState.value.currentUserId
        if (userId.isEmpty()) return

        viewModelScope.launch {
            songRepository.removeFavorite(userId, songId)
            // Actualizar la lista optimistamente
            _uiState.update { state ->
                state.copy(
                    favoriteSongs = state.favoriteSongs.filter { it.songId != songId }
                )
            }
        }
    }

    fun isFavorite(songId: String): Boolean {
        return _uiState.value.favoriteSongs.any { it.songId == songId }
    }
}