package com.example.soymusicreviewapp.data.repository

import android.util.Log
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toReview
import javax.inject.Inject
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewFirestoreDataSourceImpl,
    private val userRepository: UserRepository,
    private val songRepository: SongRepository,
    private val authRepository: AuthRepository
) {

    suspend fun getReviews(): Result<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return try {
            val reviews = remoteDataSource.getAllReviews(currentUserId)
            val reviewInfo = reviews.map { it.toReview() }
            Result.success(reviewInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(reviewId: String): Result<Review> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return try {
            val reviewDto = remoteDataSource.getReviewById(reviewId, currentUserId)
            Result.success(reviewDto.toReview())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserReviews(userId: String): Result<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return try {
            val reviews = remoteDataSource.getUserReviews(userId, currentUserId)
            val reviewInfo = reviews.map { it.toReview() }
            Result.success(reviewInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getReviewsLive(): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return remoteDataSource.listenAllReviews(currentUserId).map { reviews ->
            reviews.map { it.toReview() }
        }
    }

    fun getUserReviewsLive(userId: String): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return remoteDataSource.listenUserReviews(userId, currentUserId).map { reviews ->
            reviews.map { it.toReview() }
        }
    }

    fun getSongReviewsLive(songId: String): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return remoteDataSource.listenSongReviews(songId, currentUserId).map { reviews ->
            reviews.map { it.toReview() }
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

    suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String): Result<Unit> {
        return try {
            remoteDataSource.sendOrDeleteReviewLike(reviewId, userId)
            Result.success(Unit)
        } catch (e: Exception) {
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
            val currentUserId = authRepository.currentUser?.uid ?: ""
            val currentReview = remoteDataSource.getReviewById(reviewId, currentUserId)

            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

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
