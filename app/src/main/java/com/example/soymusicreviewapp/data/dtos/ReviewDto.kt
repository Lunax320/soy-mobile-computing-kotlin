package com.example.soymusicreviewapp.data.dtos
import com.example.soymusicreviewapp.data.Review

data class UserDto(
    val id: String = "",
    val username: String = "",
    val name: String = "",
    val profileImage: String? = null
)

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
    return Review(
        usernameId = user?.id ?: userId,
        userId = userId,
        profileImage = user?.profileImage ?: "",
        userName = user?.username ?: "Usuario Desconocido",
        date = createdAt,
        songName = songName,
        songId = songId,
        artistName = artistName,
        reviewText = reviewText,
        rating = rating
    )
}