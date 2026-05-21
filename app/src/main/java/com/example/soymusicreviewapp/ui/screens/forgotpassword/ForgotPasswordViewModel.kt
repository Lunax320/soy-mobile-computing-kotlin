package com.example.soymusicreviewapp.ui.screens.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(showMessage = false) }
    }

    fun sendResetEmail() {
        val email = _uiState.value.email.trim()

        if (email.isEmpty()) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    message = "Por favor ingresa tu correo electronico",
                    isSuccess = false
                )
            }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    message = "Por favor ingresa un correo electrónico valido (ejemplo: usuario@dominio.com)",
                    isSuccess = false
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, showMessage = false) }

        viewModelScope.launch {
            val result = authRepository.resetPassword(email)

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showMessage = true,
                        message = "¡Correo enviado! Revisa tu bandeja de entrada (o spam)",
                        isSuccess = true
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                val errorMessage = when (exception) {
                    is FirebaseAuthInvalidUserException -> {
                        "No existe ninguna cuenta con el correo '$email'. ¿Ya te has registrado?"
                    }
                    else -> {
                        "Error: ${exception?.message ?: "No se pudo enviar el correo. Verifica tu conexión."}"
                    }
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showMessage = true,
                        message = errorMessage,
                        isSuccess = false
                    )
                }
            }
        }
    }
}