package com.example.soymusicreviewapp.ui.screens.followingdetail

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
class FollowingDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowingDetailState())
    val uiState: StateFlow<FollowingDetailState> = _uiState.asStateFlow()

    fun loadFollowing(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = userRepository.getFollowing(userId)
            if (result.isSuccess) {
                _uiState.update { it.copy(
                    following = result.getOrDefault(emptyList()),
                    isLoading = false,
                    errorMessage = null
                ) }
            } else {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Error loading following"
                ) }
            }
        }
    }
}
