package com.example.soymusicreviewapp.data.dtos

data class CreateReviewDto(
    val userId: String,
    val songId: String,
    val songName: String,
    val artistName: String,
    val reviewText: String,
    val rating: Int,
    val date: String,
    val parentId: String?,
    val user: UserDto?,
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    constructor() : this(
        userId = "",
        songId = "",
        songName = "",
        artistName = "",
        reviewText = "",
        rating = 0,
        date = "",
        parentId = null,
        user = null,
        latitude = null,
        longitude = null
    )
}
