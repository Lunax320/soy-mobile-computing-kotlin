package com.example.soymusicreviewapp.data

import androidx.annotation.DrawableRes

data class Song(
    val songId: Int,
    @DrawableRes val imageId: Int,
    val name: String,
    val artist: String,
    val genre: String,
    val duration: String
)