package com.example.soymusicreviewapp.data.repository

import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.datasource.impl.ReviewRetrofitDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toReview
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRetrofitDataSourceImpl
) {
    // LEER
    suspend fun getReviews(): Result<List<Review>> {
        return try {
            val reviews = remoteDataSource.getAllReviews()
            Result.success(reviews.map { it.toReview() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // CREAR
    suspend fun createReview(songId: String, reviewText: String, rating: Int): Result<Unit> {
        return try {
            val createReviewDto = CreateReviewDto(
                userId = 1, // <--- ID QUEMADO
                songId = songId.toInt(),
                reviewText = reviewText,
                rating = rating,
                date = "2026-04-10",
                parentId = null
            )
            remoteDataSource.createReview(createReviewDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ELIMINAR
    suspend fun deleteReview(reviewId: String): Result<Unit> {
        return try {
            remoteDataSource.deleteReview(reviewId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // MODIFICAR
    suspend fun updateReview(reviewId: String, songId: String, reviewText: String, rating: Int): Result<Unit> {
        return try {
            val updateDto = CreateReviewDto(
                userId = 1, // <--- ID QUEMADO
                songId = songId.toInt(),
                reviewText = reviewText,
                rating = rating,
                date = "2026-04-10",
                parentId = null
            )
            remoteDataSource.updateReview(reviewId, updateDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}