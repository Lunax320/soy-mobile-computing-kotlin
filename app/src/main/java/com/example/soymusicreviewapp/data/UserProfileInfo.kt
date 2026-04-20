package com.example.soymusicreviewapp.data

data class UserProfileInfo(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val profileImageUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val followed: Boolean = false
)
