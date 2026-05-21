package com.example.soymusicreviewapp.ui.screens.forgotpassword

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val showMessage: Boolean = false,
    val message: String = "",
    val isSuccess: Boolean = false
)