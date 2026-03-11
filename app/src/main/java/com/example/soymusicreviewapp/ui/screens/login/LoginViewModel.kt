package com.example.soymusicreviewapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onUserChange(newText: String) {
        _uiState.update { it.copy(userText = newText) }
    }

    fun onPasswordChange(newText: String) {
        _uiState.update { it.copy(passwordText = newText) }
    }

    fun togglePasswordVisibility() {
        val currentValue = _uiState.value.showPassword
        _uiState.update { it.copy(showPassword = !currentValue) }
    }

    fun onLoginButtonPressed() {
        val currentState = _uiState.value

        if(
            currentState.userText.isNullOrEmpty() ||
            currentState.passwordText.isNullOrEmpty()
        ) {
            _uiState.update { it.copy(showMessage = true, errorMessage = "All fields are required") }
        } else {
            viewModelScope.launch {
                try {
                    authRepository.singIn(
                        currentState.userText,
                        currentState.passwordText
                    )
                    _uiState.update { it.copy(navigate = true) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = "Login failed", showMessage = true) }
                }
            }
        }
    }
}