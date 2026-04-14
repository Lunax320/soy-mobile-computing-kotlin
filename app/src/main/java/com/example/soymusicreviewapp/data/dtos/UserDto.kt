package com.example.soymusicreviewapp.data.dtos

data class UserDto(
    val id: String = "",
    val username: String = "",
    val name: String = "",
    val profileImage: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0
)