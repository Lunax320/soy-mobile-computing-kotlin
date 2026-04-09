package com.example.soymusicreviewapp.data.dtos
import com.example.soymusicreviewapp.data.Review

data class UserDto(
    val id: String,
    val username: String,
    val name: String,
    val profileImage: String?
)

data class ReviewDto(
    val id: String,
    val userId: String,
    val songName: String,
    val artistName: String,
    val reviewText: String,
    val rating: Int,
    val createdAt: String,
    val updatedAt: String,
    val user: UserDto
)

// Función de mapeo
fun ReviewDto.toReview(): Review {
    return Review(
        usernameId = user.id,
        profileImage = user.profileImage ?: "",
        userName = user.username,
        date = createdAt,
        songName = songName,
        artistName = artistName,
        reviewText = reviewText,
        rating = rating
    )
}