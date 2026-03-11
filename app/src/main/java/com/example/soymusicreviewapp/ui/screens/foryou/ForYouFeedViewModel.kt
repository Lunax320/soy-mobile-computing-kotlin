package com.example.soymusicreviewapp.ui.screens.foryou

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ForYouFeedViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(ForYouFeedState())

    val uiState: StateFlow<ForYouFeedState> = _uiState.asStateFlow()

    init {
        loadReviews()
    }

    // Loads recommended reviews from the local provider
    private fun loadReviews() {
        val reviews = LocalReviewProvider.reviews
        _uiState.update { currentState ->
            currentState.copy(reviews = reviews)
        }
    }
}