package com.example.soymusicreviewapp.data.dtos

import com.example.soymusicreviewapp.data.Review

data class ReviewDto(
    val id: String = "",
    val userId: String = "",
    val songId: String = "",
    val songName: String = "",
    val artistName: String = "",
    val reviewText: String = "",
    val rating: Int = 0,
    val createdAt: String = "",
    val updatedAt: String = "",
    val user: UserDto? = null
)

fun ReviewDto.toReview(): Review {

    var finalUsernameId = userId
    if (user != null) {
        finalUsernameId = user.id
    }

    var finalProfileImage = ""
    if (user != null) {
        if (user.profileImage != null) {
            finalProfileImage = user.profileImage
        }
    }

    var finalUserName = "Usuario Desconocido"
    if (user != null) {
        finalUserName = user.username
    }

    return Review(
        usernameId = finalUsernameId,
        userId = userId,
        profileImage = finalProfileImage,
        userName = finalUserName,
        date = createdAt,
        songName = songName,
        songId = songId,
        artistName = artistName,
        reviewText = reviewText,
        rating = rating
    )
}