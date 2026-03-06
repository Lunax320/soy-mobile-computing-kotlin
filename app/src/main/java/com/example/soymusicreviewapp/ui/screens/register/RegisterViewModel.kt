package com.example.soymusicreviewapp.ui.screens.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun onNameChange(newText: String) {
        _uiState.update { it.copy(nameText = newText) }
    }

    fun onUserChange(newText: String) {
        _uiState.update { it.copy(userText = newText) }
    }

    fun onEmailChange(newText: String) {
        _uiState.update { it.copy(emailText = newText) }
    }

    fun onPasswordChange(newText: String) {
        _uiState.update { it.copy(passwordText = newText) }
    }
}
