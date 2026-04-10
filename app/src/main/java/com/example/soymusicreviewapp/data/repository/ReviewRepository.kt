package com.example.soymusicreviewapp.data.repository

import android.util.Log
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.datasource.impl.ReviewRetrofitDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toReview
import javax.inject.Inject
import coil.network.HttpException
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

    suspend fun createReview(
        userId: Int = 1,
        songId: Int,
        reviewText: String,
        rating: Int,
        date: String,
        parentId: Int? = null
    ): Result<Unit> {
        Log.d("API_TRACKER", "Repositorio: Iniciando creación de reseña para la canción con identificador: " + songId)
        Log.d("API_TRACKER", "Repositorio: Texto recibido: " + reviewText)

        return try {
            val createReviewDto = CreateReviewDto(
                userId = userId,
                songId = songId,
                reviewText = reviewText,
                rating = rating,
                date = date,
                parentId = parentId
            )
            Log.d("API_TRACKER", "Repositorio: Objeto de transferencia construido correctamente. Enviando al servidor...")

            remoteDataSource.createReview(createReviewDto)

            Log.d("API_TRACKER", "Repositorio: El servidor aceptó la creación de la reseña sin errores.")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("API_TRACKER", "Repositorio: El servidor rechazó la creación. Motivo: " + e.message)
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: String): Result<Unit> {
        Log.d("API_TRACKER", "Repositorio: Intentando eliminar la reseña con identificador: " + reviewId)
        return try {
            remoteDataSource.deleteReview(reviewId)
            Log.d("API_TRACKER", "Repositorio: Reseña eliminada con éxito en el servidor.")
            Result.success(Unit)
        } catch(e: Exception) {
            Log.e("API_TRACKER", "Repositorio: Fallo al intentar eliminar. Motivo: " + e.message)
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

            var songIdentifier = 0
            val parsedSongId = songId.toIntOrNull()
            if (parsedSongId != null) {
                songIdentifier = parsedSongId
            }

            val updateDto = CreateReviewDto(
                userId = 1,
                songId = songIdentifier,
                reviewText = reviewText,
                rating = rating,
                date = currentDate,
                parentId = null
            )

            remoteDataSource.updateReview(reviewId, updateDto)
            Log.d("API_TRACKER", "Repositorio: Actualización completada en el servidor.")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("API_TRACKER", "Repositorio: Error crítico al actualizar. Motivo: " + e.message)
            Result.failure(e)
        }
    }
}