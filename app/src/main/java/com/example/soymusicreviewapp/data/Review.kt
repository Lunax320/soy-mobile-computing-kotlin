package com.example.soymusicreviewapp.data

import androidx.annotation.DrawableRes

data class Review(
    val usernameId: String,
    val profileImage: String,
    val userName: String,
    val date: String,
    val songName: String,
    val artistName: String,
    val reviewText: String,
    val rating: Int
)