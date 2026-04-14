package com.example.soymusicreviewapp.data.datasource.services

import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.ReviewDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewRetrofitService {
    @GET("reviews")
    suspend fun getAllReviews(): List<ReviewDto>

    @POST("reviews")
    suspend fun createReview(@Body review: CreateReviewDto): Unit

    @DELETE("reviews/{reviewId}")
    suspend fun deleteReview(@Path("reviewId") reviewId: String): Unit

    @PUT("reviews/{reviewId}")
    suspend fun updateReview(@Path("reviewId") reviewId: String, @Body review: CreateReviewDto): Unit

    @GET("reviews/{reviewId}")
    suspend fun getReviewById(@Path("reviewId") reviewId: String): ReviewDto
}