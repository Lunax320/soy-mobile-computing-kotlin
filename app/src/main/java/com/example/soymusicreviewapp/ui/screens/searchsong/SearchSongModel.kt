package com.example.soymusicreviewapp.ui.screens.searchsong

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSongModel @Inject constructor(
    private val songRepository: SongRepository // Se inyecta el repositorio
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchSongState())
    val uiState: StateFlow<SearchSongState> = _uiState.asStateFlow()

    init {
        loadSongs()
    }

    fun onSearchChange(newText: String) {
        _uiState.update { currentState ->
            currentState.copy(searchText = newText)
        }
    }

    private fun loadSongs() {
        viewModelScope.launch {

            val result = songRepository.getSongs()

            if (result.isSuccess) {
                val backendSongs = result.getOrNull() ?: emptyList()
                Log.d("API_TRACKER", "Se cargaron ${backendSongs.size} canciones")

                _uiState.update { currentState ->
                    currentState.copy(songs = backendSongs)
                }
            } else {
                Log.e("API_TRACKER", "Fallo al descargar canciones")
            }
        }
    }
}