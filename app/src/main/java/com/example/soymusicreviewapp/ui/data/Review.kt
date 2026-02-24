package com.example.soymusicreviewapp.ui.data

import androidx.annotation.DrawableRes

data class Review(
    @DrawableRes val userImageId: Int,
    val userName: String,
    val date: String,
    val songName: String,
    val artistName: String,
    val reviewText: String,
    val rating: Int
)