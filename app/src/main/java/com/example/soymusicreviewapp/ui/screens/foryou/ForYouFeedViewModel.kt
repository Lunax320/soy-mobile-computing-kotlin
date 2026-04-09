package com.example.soymusicreviewapp.ui.screens.foryou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForYouFeedViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ForYouFeedState())
    val uiState: StateFlow<ForYouFeedState> = _uiState.asStateFlow()

    init {
        loadReviews()
    }

    private fun loadReviews() {
        viewModelScope.launch {
            val reviewsResult = reviewRepository.getReviews()

            if (reviewsResult.isSuccess) {
                _uiState.update { currentState ->
                    currentState.copy(reviews = reviewsResult.getOrNull() ?: emptyList())
                }
            } else {
                android.util.Log.e("API_ERROR", "Error al descargar reseñas For You: ${reviewsResult.exceptionOrNull()?.message}")
            }
        }
    }
}