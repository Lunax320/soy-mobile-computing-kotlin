package com.example.soymusicreviewapp.ui.screens.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingFeedViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(FollowingFeedState())

    val uiState: StateFlow<FollowingFeedState> = _uiState.asStateFlow()

    private fun loadReviews() {
        viewModelScope.launch{
            val reviews = reviewRepository.getReviews()
            if (reviews.isSuccess) {
                _uiState.update { it.copy(reviews = reviews.getOrNull() ?: (emptyList())) }
            } else {
                // error en la consola
                android.util.Log.e("API_ERROR", "Error al descargar reseñas: ${reviews.exceptionOrNull()?.message}")
            }
        }
    }

    init {
        loadReviews()
    }
}