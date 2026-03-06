package com.example.soymusicreviewapp.ui.screens.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())

    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    // Updates the username text in the state
    fun onUserChange(newText: String) {
        _uiState.update { currentState ->
            currentState.copy(userText = newText)
        }
    }

    // Updates the password text in the state
    fun onPasswordChange(newText: String) {
        _uiState.update { currentState ->
            currentState.copy(passwordText = newText)
        }
    }
}