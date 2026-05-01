package com.example.soymusicreviewapp.data.dtos

import com.example.soymusicreviewapp.data.UserProfileInfo

data class UserDto(
    val id: String,
    val username: String,
    val name: String,
    val profileImage: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    var followed: Boolean = false
) {
    constructor() : this("", "", "")
}

fun UserDto.toUserProfileInfo(): UserProfileInfo {
    return UserProfileInfo(
        id = id,
        name = name,
        username = username,
        profileImageUrl = profileImage,
        followed = followed
    )
}
