package com.example.soymusicreviewapp.ui.screens.followersDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowersDetailState())
    val uiState: StateFlow<FollowersDetailState> = _uiState.asStateFlow()

    fun loadFollowers(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = userRepository.getFollowers(userId)
            if (result.isSuccess) {
                _uiState.update { it.copy(
                    followers = result.getOrDefault(emptyList()),
                    isLoading = false,
                    errorMessage = null
                ) }
            } else {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Error loading followers"
                ) }
            }
        }
    }
}
