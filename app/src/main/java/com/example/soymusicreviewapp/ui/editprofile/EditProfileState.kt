package com.example.soymusicreviewapp.ui.editprofile

data class EditProfileState(
    val name: String = "",
    val username: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)