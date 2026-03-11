package com.example.soymusicreviewapp.ui.screens.register

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun onNameChange(newText: String) {
        _uiState.update { it.copy(nameText = newText) }
    }

    fun onEmailChange(newText: String) {
        _uiState.update { it.copy(emailText = newText) }
    }

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

    fun onRegisterButtonPressed() {
        val currentState = _uiState.value

        if (currentState.passwordText.isNullOrEmpty() || currentState.emailText.isNullOrEmpty() || currentState.userText.isNullOrEmpty() || currentState.nameText.isNullOrEmpty()) {
            _uiState.update { it.copy(showMessage = true, errorMessage = "All fields are required") }
        } else {
            if (currentState.passwordText.length < 6) {
                _uiState.update { it.copy(showMessage = true, errorMessage = "The password must be at least 6 characters long") }
            } else {
                if (currentState.emailText == "admin@admin.com") {
                    _uiState.update { it.copy(showMessage = true, errorMessage = "The email is already in use") }
                } else {
                    viewModelScope.launch {
                        try {
                            authRepository.signUp(currentState.emailText, currentState.passwordText)
                            _uiState.update { it.copy(navigate = true) }
                        } catch (e: Exception) {
                            _uiState.update { it.copy(errorMessage = e.message.toString(), showMessage = true) }
                        }
                    }
                }
            }
        }
    }
}