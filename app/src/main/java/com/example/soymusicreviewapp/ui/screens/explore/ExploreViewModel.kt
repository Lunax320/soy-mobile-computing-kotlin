package com.example.soymusicreviewapp.ui.screens.explore

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
class ExploreViewModel @Inject constructor(
    private val songRepository: SongRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ExploreState())
    val uiState: StateFlow<ExploreState> = _uiState.asStateFlow()

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
                val allSongs = result.getOrNull() ?: emptyList()
                _uiState.update { currentState ->
                    currentState.copy(songs = allSongs)
                }
            }
        }
    }
}