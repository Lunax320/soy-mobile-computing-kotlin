package com.example.soymusicreviewapp.ui.data

import androidx.annotation.DrawableRes

data class Song(
    @DrawableRes val imageId: Int,
    val name: String,
    val artist: String,
    val genre: String,
    val duration: String
)