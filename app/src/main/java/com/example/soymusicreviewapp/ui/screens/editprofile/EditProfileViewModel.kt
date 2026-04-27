package com.example.soymusicreviewapp.ui.screens.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState: StateFlow<EditProfileState> = _uiState.asStateFlow()

    init {
        loadCurrentUserData()
    }

    private fun loadCurrentUserData() {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            val result = userRepository.getUserById(userId)
            if (result.isSuccess) {
                val user = result.getOrNull()
                _uiState.update { it.copy(
                    name = user?.name ?: "",
                    username = user?.username ?: ""
                ) }
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun updateProfile() {
        val userId = authRepository.currentUser?.uid ?: return
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = userRepository.updateUserInfo(userId, currentState.name, currentState.username)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }
}