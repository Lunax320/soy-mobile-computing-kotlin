package com.example.soymusicreviewapp.data.dtos

import com.example.soymusicreviewapp.data.Review

data class ReviewDto(
    val id: String,
    val userId: String,
    val songId: String,
    val songName: String,
    val artistName: String,
    val reviewText: String,
    val rating: Int,
    val date: String,
    val createdAt: String,
    val updatedAt: String,
    val parentId: String?,
    val user: UserDto?,
    val likesCount : Int,
    val liked: Boolean
) {
    constructor() : this(
        id = "",
        userId = "",
        songId = "",
        songName = "",
        artistName = "",
        reviewText = "",
        rating = 0,
        date = "",
        createdAt = "",
        updatedAt = "",
        parentId = null,
        user = null,
        likesCount = 0,
        liked = false
    )
}

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

    val finalDate = if (createdAt.isNotEmpty()) createdAt else date

    return Review(
        id = id,
        usernameId = finalUsernameId,
        userId = userId,
        profileImage = finalProfileImage,
        userName = finalUserName,
        date = finalDate,
        songName = songName,
        songId = songId,
        artistName = artistName,
        reviewText = reviewText,
        rating = rating,
        parentId = parentId,
        likesCount = likesCount,
        liked = liked
    )
}
