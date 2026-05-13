package com.example.soymusicreviewapp.data

data class Review(
    val id: String,
    val usernameId: String,
    val userId: String,
    val profileImage: String,
    val userName: String,
    val date: String,
    val songName: String,
    val songId: String,
    val artistName: String,
    val reviewText: String,
    val rating: Int,
    val parentId: String? = null,
    val likesCount: Int = 0,
    val liked: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null
)
