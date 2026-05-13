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
    private val reviewDataSource: ReviewFirestoreDataSourceImpl,
    private val userRepository: UserRepository,
    private val songRepository: SongRepository,
    private val authRepository: AuthRepository
) {

    suspend fun getReviews(): Result<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return try {
            val reviews = reviewDataSource.getAllReviews(currentUserId)
            val reviewInfo = reviews.map { it.toReview() }
            Result.success(reviewInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(reviewId: String): Result<Review> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return try {
            val reviewDto = reviewDataSource.getReviewById(reviewId, currentUserId)
            Result.success(reviewDto.toReview())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserReviews(userId: String): Result<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return try {
            val reviews = reviewDataSource.getUserReviews(userId, currentUserId)
            val reviewInfo = reviews.map { it.toReview() }
            Result.success(reviewInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getReviewsLive(): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return reviewDataSource.listenAllReviews(currentUserId).map { reviews ->
            reviews.map { it.toReview() }
        }
    }

    fun getUserReviewsLive(userId: String): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return reviewDataSource.listenUserReviews(userId, currentUserId).map { reviews ->
            reviews.map { it.toReview() }
        }
    }

    fun getSongReviewsLive(songId: String): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return reviewDataSource.listenSongReviews(songId, currentUserId).map { reviews ->
            reviews.map { it.toReview() }
        }
    }

    suspend fun createReview(review: CreateReviewDto): Result<Unit> {
        return try {
            val userProfile = if (review.user == null) {
                userRepository.getUserById(review.userId).getOrNull()
            } else {
                review.user
            }

            val enrichedReview = review.copy(user = userProfile)

            reviewDataSource.createReview(enrichedReview)
            Log.d("API_TRACKER", "Reseña creada con éxito")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("API_TRACKER", "Fallo al crear la reseña: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: String): Result<Unit> {
        return try {
            reviewDataSource.deleteReview(reviewId)
            Result.success(Unit)
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String): Result<Unit> {
        return try {
            reviewDataSource.sendOrDeleteReviewLike(reviewId, userId)
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
            val currentReview = reviewDataSource.getReviewById(reviewId, currentUserId)

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

            reviewDataSource.updateReview(reviewId, updateDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCommentsForReview(parentReviewId: String): Result<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return try {
            val comments = reviewDataSource.getCommentsForReview(parentReviewId, currentUserId)
            Result.success(comments.map { it.toReview() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCommentsForReviewLive(parentReviewId: String): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return reviewDataSource.listenCommentsForReview(parentReviewId, currentUserId)
            .map { comments -> comments.map { it.toReview() } }
    }

    suspend fun createComment(
        parentReviewId: String,
        userId: String,
        commentText: String,
        date: String
    ): Result<Unit> {
        return try {
            val parentReviewResult = getReviewById(parentReviewId)
            val parentReview = parentReviewResult.getOrNull()

            if (parentReview == null) {
                return Result.failure(Exception("No se encontró la review original"))
            }

            val userResult = userRepository.getUserById(userId)
            val userProfile = userResult.getOrNull()

            val createCommentDto = CreateReviewDto(
                userId = userId,
                songId = parentReview.songId,
                songName = parentReview.songName,
                artistName = parentReview.artistName,
                reviewText = commentText,
                rating = 0,
                date = date,
                parentId = parentReviewId,
                user = userProfile
            )

            reviewDataSource.createReview(createCommentDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getMainReviewsLive(): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return reviewDataSource.listenAllReviews(currentUserId).map { reviews ->
            reviews
                .map { it.toReview() }
                .filter { it.parentId == null }
        }
    }

    fun getCommentsLive(): Flow<List<Review>> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return reviewDataSource.listenAllReviews(currentUserId).map { reviews ->
            reviews
                .map { it.toReview() }
                .filter { it.parentId != null }
        }
    }

    suspend fun getReviewsForMap(): List<Review> {
        return try {
            val reviewsDto = reviewDataSource.getLast24ReviewsWithLocation()
            reviewsDto.map { it.toReview() }
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Error al obtener reseñas para el mapa: ${e.message}")
            emptyList()
        }
    }
}
