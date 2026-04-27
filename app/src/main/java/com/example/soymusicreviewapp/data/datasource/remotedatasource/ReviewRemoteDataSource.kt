package com.example.soymusicreviewapp.data.datasource.remotedatasource
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.ReviewDto
import kotlinx.coroutines.flow.Flow

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(currentUserId: String = ""): List<ReviewDto>
    suspend fun getReviewById(reviewId: String, currentuserId: String = ""): ReviewDto
    suspend fun createReview(review: CreateReviewDto): Unit
    suspend fun deleteReview(reviewId: String): Unit
    suspend fun updateReview(reviewId: String, review: CreateReviewDto): Unit
    suspend fun getUserReviews(userId: String, currentUserId: String = ""): List<ReviewDto>
    suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String): Unit
    fun listenAllReviews(currentUserId: String = ""): Flow<List<ReviewDto>>
    fun listenUserReviews(userId: String, currentUserId: String = ""): Flow<List<ReviewDto>>
    fun listenSongReviews(songId: String, currentUserId: String = ""): Flow<List<ReviewDto>>
    suspend fun getCommentsForReview(parentReviewId: String, currentUserId: String = ""): List<ReviewDto>
    fun listenCommentsForReview(parentReviewId: String, currentUserId: String = ""): Flow<List<ReviewDto>>
}
