package com.example.soymusicreviewapp.ui.screens.explore

import android.util.Log
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
class ExploreViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreState())
    val uiState: StateFlow<ExploreState> = _uiState.asStateFlow()

    init {
        loadSongs()
        loadFavorites()
    }

    fun onSearchChange(newText: String) {
        _uiState.update { it.copy(searchText = newText) }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            songRepository.getSongsLive()
                .catch { e -> Log.e("ExploreVM", "Error loading songs: ${e.message}") }
                .collect { songs ->
                    _uiState.update { it.copy(songs = songs) }
                }
        }
    }

    private fun loadFavorites() {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            songRepository.getFavoriteSongsLive(userId)
                .catch { e -> Log.e("ExploreVM", "Error loading favorites: ${e.message}") }
                .collect { favoriteSongs ->
                    val favoriteIds = favoriteSongs.map { it.songId }.toSet()
                    _uiState.update { it.copy(favoriteSongsIds = favoriteIds) }
                }
        }
    }

    fun onFavoriteClick(songId: String) {
        val userId = authRepository.currentUser?.uid ?: return
        val isFavorite = _uiState.value.favoriteSongsIds.contains(songId)

        // Actualización optimista
        val newFavoriteIds = if (isFavorite) {
            _uiState.value.favoriteSongsIds.minus(songId)
        } else {
            _uiState.value.favoriteSongsIds.plus(songId)
        }
        _uiState.update { it.copy(favoriteSongsIds = newFavoriteIds) }

        viewModelScope.launch {
            val result = if (isFavorite) {
                songRepository.removeFavorite(userId, songId)
            } else {
                songRepository.addFavorite(userId, songId)
            }
            if (result.isFailure) {
                // Revertir en caso de error
                _uiState.update { it.copy(favoriteSongsIds = _uiState.value.favoriteSongsIds) }
            }
        }
    }
}