package com.example.soymusicreviewapp.data.repository

import android.util.Log
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.datasource.impl.ReviewRetrofitDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toReview
import javax.inject.Inject
import coil.network.HttpException
import com.example.soymusicreviewapp.data.datasource.remotedatasource.ReviewRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRetrofitDataSourceImpl
) {

    suspend fun getReviews(): Result<List<Review>> {
        return try {
            val reviews = remoteDataSource.getAllReviews()
            val reviewInfo = reviews.map { it.toReview() }
            Result.success(reviewInfo)
        } catch (e: HttpException) {
            Log.e("API_TRACKER", "Error HTTP al descargar reseñas: " + e.message)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("API_TRACKER", "Error general al descargar reseñas: " + e.message)
            Result.failure(e)
        }
    }

    suspend fun getUserReviews(userId: String): Result<List<Review>> {
        return try {
            val reviews = remoteDataSource.getUserReviews(userId)
            val reviewInfo = reviews.map { it.toReview() }
            Result.success(reviewInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(
        userId: String = "",
        songId: String,
        reviewText: String,
        rating: Int,
        date: String,
        parentId: String? = null
    ): Result<Unit> {
        Log.d("API_TRACKER", "Repositorio: Iniciando creación de reseña para la canción con identificador: " + songId)

        return try {
            val createReviewDto = CreateReviewDto(
                userId = userId,
                songId = songId,
                reviewText = reviewText,
                rating = rating,
                date = date,
                parentId = parentId
            )
            remoteDataSource.createReview(createReviewDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("API_TRACKER", "Repositorio: El servidor rechazó la creación. Motivo: " + e.message)
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: String): Result<Unit> {
        return try {
            remoteDataSource.deleteReview(reviewId)
            Result.success(Unit)
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(
        reviewId: String,
        songId: String,
        reviewText: String,
        rating: Int
    ): Result<Unit> {
        Log.d("API_TRACKER", "Repositorio: Iniciando actualización de la reseña: " + reviewId)

        return try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val updateDto = CreateReviewDto(
                userId = "1",
                songId = songId,
                reviewText = reviewText,
                rating = rating,
                date = currentDate,
                parentId = null
            )

            remoteDataSource.updateReview(reviewId, updateDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}