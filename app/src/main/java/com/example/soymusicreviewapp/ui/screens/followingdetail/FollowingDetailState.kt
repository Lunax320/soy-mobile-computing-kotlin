package com.example.soymusicreviewapp.ui.screens.followingdetail

import com.example.soymusicreviewapp.data.dtos.UserDto

data class FollowingDetailState(
    val following: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
