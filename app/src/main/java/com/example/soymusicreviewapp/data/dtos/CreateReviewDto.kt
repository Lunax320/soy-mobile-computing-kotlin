package com.example.soymusicreviewapp.data.dtos

data class CreateReviewDto(
    val userId: String = "",
    val songId: String = "",
    // Desnormalizacion cancion
    val songName: String = "",
    val artistName: String = "",
    val reviewText: String = "",
    val rating: Int = 0,
    val date: String = "",
    val parentId: String? = null,
    // Desnormalización suario
    val user: UserDto? = null
)