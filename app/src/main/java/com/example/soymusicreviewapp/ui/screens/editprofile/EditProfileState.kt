package com.example.soymusicreviewapp.ui.screens.editprofile

data class EditProfileState(
    val name: String = "",
    val username: String = "",
    val profileImageUrl: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)