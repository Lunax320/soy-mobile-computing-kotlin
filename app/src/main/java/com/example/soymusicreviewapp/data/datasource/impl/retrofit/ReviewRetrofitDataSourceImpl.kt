package com.example.soymusicreviewapp.data.datasource.impl.retrofit

import com.example.soymusicreviewapp.data.datasource.remotedatasource.ReviewRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.services.ReviewRetrofitService
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.ReviewDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    val service: ReviewRetrofitService
) : ReviewRemoteDataSource {

    override suspend fun getAllReviews(currentUserId: String): List<ReviewDto>{
        return service.getAllReviews()
    }

    override suspend fun getReviewById (reviewId: String, currentuserId: String): ReviewDto {
        return service.getReviewById(reviewId)
    }

    override suspend fun createReview(review: CreateReviewDto) {
        service.createReview(review)
    }

    override suspend fun deleteReview(reviewId: String){
        service.deleteReview(reviewId)
    }

    override suspend fun updateReview(reviewId: String, review: CreateReviewDto){
        service.updateReview(reviewId, review)
    }

    override suspend fun getUserReviews(userId: String, currentUserId: String): List<ReviewDto> {
        return emptyList()
    }

    override suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override fun listenAllReviews(currentUserId: String): Flow<List<ReviewDto>> {
        TODO("Not yet implemented")
    }

    override fun listenUserReviews(
        userId: String,
        currentUserId: String
    ): Flow<List<ReviewDto>> {
        TODO("Not yet implemented")
    }

    override fun listenSongReviews(
        songId: String,
        currentUserId: String
    ): Flow<List<ReviewDto>> {
        TODO("Not yet implemented")
    }

}
