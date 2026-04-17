package com.example.soymusicreviewapp.data.repository

import android.util.Log
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toReview
import javax.inject.Inject
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewFirestoreDataSourceImpl,
    private val userRepository: UserRepository,
    private val songRepository: SongRepository
) {

    suspend fun getReviews(): Result<List<Review>> {
        return try {
            val reviews = remoteDataSource.getAllReviews()
            val reviewInfo = reviews.map { it.toReview() }
            Result.success(reviewInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(reviewId: String): Result<Review> {
        return try {
            val reviewDto = remoteDataSource.getReviewById(reviewId)
            Result.success(reviewDto.toReview())
        } catch (e: Exception) {
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

        return try {
            val userResult = userRepository.getUserById(userId)
            val userProfile = userResult.getOrNull()

            val songResult = songRepository.getSongById(songId)
            val songDetails = songResult.getOrNull()

            val createReviewDto = CreateReviewDto(
                userId = userId,
                songId = songId,
                songName = songDetails?.name ?: "Cancion Desconocida",
                artistName = songDetails?.artist ?: "Artista Desconocido",
                reviewText = reviewText,
                rating = rating,
                date = date,
                parentId = parentId,
                user = userProfile
            )

            remoteDataSource.createReview(createReviewDto)
            Log.d("API_TRACKER", "Reseña normalizados")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("API_TRACKER", "Fallo al crear la reseña")
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
        return try {
            // 1. Obtenemos la reseña actual para no perder los datos del usuario
            val currentReview = remoteDataSource.getReviewById(reviewId)
            
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            
            // 2. Creamos el DTO de actualización manteniendo el usuario original
            val updateDto = CreateReviewDto(
                userId = currentReview.userId ?: "",
                songId = songId,
                songName = currentReview.songName ?: "",
                artistName = currentReview.artistName ?: "",
                reviewText = reviewText,
                rating = rating,
                date = currentDate,
                parentId = currentReview.parentId,
                user = currentReview.user
            )

            remoteDataSource.updateReview(reviewId, updateDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}