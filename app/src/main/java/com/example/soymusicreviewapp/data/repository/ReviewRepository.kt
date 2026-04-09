package com.example.soymusicreviewapp.data.repository

import android.adservices.adid.AdId
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.datasource.ReviewRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.impl.ReviewRetrofitDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toReview
import javax.inject.Inject
import coil.network.HttpException
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.ui.screens.searchsong.CreateReviewScreen

class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRetrofitDataSourceImpl
) {
    suspend fun getReviews(): Result<List<Review>> {
        return try {
            val reviews = remoteDataSource.getAllReviews()
            val reviewInfo = reviews.map { it.toReview() }
            Result.success(reviewInfo)

        } catch (e: HttpException) {
            Result.failure(e)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(userId: String,  songId: String,  reviewText: String,  rating: Int,  date: String,  parentId: String?): Result<Unit> {
        return try {
            val createReviewDto = CreateReviewDto(
                userId = userId.toInt(),
                songId = songId.toInt(),
                reviewText = reviewText,
                rating = rating,
                date = date,
                parentId = parentId?.toInt(),
            )
            remoteDataSource.createReview(createReviewDto)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
