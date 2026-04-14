package com.example.soymusicreviewapp.data.dtos

data class CreateReviewDto(
    val userId: String = "",
    val songId: String = "",
    val reviewText: String = "",
    val rating: Int = 0,
    val date: String = "",
    val parentId: String? = null
)