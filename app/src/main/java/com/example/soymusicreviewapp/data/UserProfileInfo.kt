package com.example.soymusicreviewapp.data

data class UserProfileInfo(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val profileImageUrl: String?,
    val followersCount: Int,
    val followingCount: Int
)