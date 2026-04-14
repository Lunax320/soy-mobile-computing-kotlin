package com.example.soymusicreviewapp.ui.screens.register

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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

    fun onMessageShown() {
        _uiState.update { it.copy(showMessage = false) }
    }

    fun onRegisterButtonPressed() {
        val currentState = _uiState.value

        if (currentState.passwordText.isNullOrEmpty() || currentState.emailText.isNullOrEmpty() || currentState.userText.isNullOrEmpty() || currentState.nameText.isNullOrEmpty()) {
            _uiState.update { it.copy(showMessage = true, errorMessage = "Todos los campos son necesarios") }
        } else {
            if (currentState.passwordText.length < 6) {
                _uiState.update { it.copy(showMessage = true, errorMessage = "La contraseña debe tener minimo 6 caracteres") }
            } else {
                if (currentState.emailText == "admin@admin.com") {
                    _uiState.update { it.copy(showMessage = true, errorMessage = "El email ya esta en uso") }
                } else {
                    viewModelScope.launch {
                        // 1. Crea la cuenta en Firebase Auth
                        val resultado = authRepository.signUp(currentState.emailText, currentState.passwordText)

                        if (resultado.isSuccess) {
                            val userId = authRepository.currentUser?.uid ?: throw Exception("No se pudo obtener el usuario actual")

                            val dbResult = userRepository.registerUser(
                                username = currentState.userText,
                                fullname = currentState.nameText,
                                userId = userId
                            )

                            if (dbResult.isSuccess) {
                                _uiState.update { it.copy(navigate = true) }
                            } else {
                                _uiState.update { it.copy(errorMessage = "Error guardando perfil", showMessage = true) }
                            }

                        } else {
                            var mensajeDeError = "Error al crear la cuenta"
                            val excepcion = resultado.exceptionOrNull()
                            if (excepcion != null && excepcion.message != null) {
                                mensajeDeError = excepcion.message.toString()
                            }
                            _uiState.update { it.copy(errorMessage = mensajeDeError, showMessage = true) }
                        }
                    }
                }
            }
        }
    }
}
