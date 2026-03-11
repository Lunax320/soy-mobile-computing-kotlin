package com.example.soymusicreviewapp.ui.screens.following

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FollowingFeedViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(FollowingFeedState())

    val uiState: StateFlow<FollowingFeedState> = _uiState.asStateFlow()

    // Initialize data loading on creation / like in twitter
    init {
        loadReviews()
    }
    private fun loadReviews() {
        val reviews = LocalReviewProvider.reviews
        _uiState.update { currentState ->
            currentState.copy(reviews = reviews)
        }
    }
}