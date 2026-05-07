package com.example.soymusicreviewapp.ui.screens.register

data class RegisterState(
    val nameText: String = "",
    val userText: String = "",
    val emailText: String = "",
    val passwordText: String = "",
    val showPassword: Boolean = false,
    val showMessage: Boolean = false,
    val errorMessage: String = "",
    val navigate: Boolean = false,
    val loading: Boolean =false
)
