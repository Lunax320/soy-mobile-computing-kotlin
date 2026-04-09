package com.example.soymusicreviewapp.data.dtos

data class CreateReviewDto(
    val userId: Int,
    val songId: Int,
    val reviewText: String,
    val rating: Int,
    val date: String,
    val parentId: Int?
)
