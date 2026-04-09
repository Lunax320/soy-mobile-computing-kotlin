package com.example.soymusicreviewapp.data.datasource.impl

import com.example.soymusicreviewapp.data.datasource.ReviewRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.services.ReviewRetrofitService
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.ReviewDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    val service: ReviewRetrofitService
) : ReviewRemoteDataSource {
    override suspend fun getAllReviews(): List<ReviewDto>{
        return service.getAllReviews()
    }

    override suspend fun getReviewById(reviewId: String): ReviewDto {
        return service.getReviewById(reviewId.toInt())
    }

    override suspend fun createReview(review: CreateReviewDto) {
       service.createReview(review)
    }

    override suspend fun deleteReview(reviewId: String){
        service.deleteReview(reviewId.toInt())
    }

    override suspend fun updateReview(reviewId: String, review: CreateReviewDto){
        service.updateReview(reviewId.toInt(), review)
    }
}