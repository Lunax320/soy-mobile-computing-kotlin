package com.example.soymusicreviewapp.data.dtos

import com.example.soymusicreviewapp.data.UserProfileInfo

data class UserDto(
    val id: String,
    val username: String,
    val name: String,
    val profileImage: String?,
    var followed: Boolean
) {
    constructor() : this(
        id = "",
        username = "",
        name = "",
        profileImage = null,
        followed = false
    )
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
