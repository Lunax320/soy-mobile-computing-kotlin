package com.example.soymusicreviewapp.data.datasource.remotedatasource
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.ReviewDto

// Dentro de ReviewRemoteDataSource.kt
interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDto>
    suspend fun getReviewById(reviewId: String): ReviewDto
    suspend fun createReview(review: CreateReviewDto): Unit
    suspend fun deleteReview(reviewId: String): Unit
    suspend fun updateReview(reviewId: String, review: CreateReviewDto): Unit
    suspend fun getUserReviews(userId: String): List<ReviewDto>
}