package com.example.soymusicreviewapp.data


data class Review(
    val usernameId: String,
    val userId: String,
    val profileImage: String,
    val userName: String,
    val date: String,
    val songName: String,
    val songId: String,
    val artistName: String,
    val reviewText: String,
    val rating: Int
)