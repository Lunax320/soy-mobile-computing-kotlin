package com.example.soymusicreviewapp.ui.screens.followersDetail

import com.example.soymusicreviewapp.data.dtos.UserDto

data class FollowersDetailState(
    val followers: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
