package com.example.soymusicreviewapp.data.datasource.impl.firestore

import com.example.soymusicreviewapp.data.datasource.remotedatasource.ReviewRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.ReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewRemoteDataSource {

    override suspend fun getAllReviews(): List<ReviewDto> {
        val snapshot = db.collection("reviews").get().await()

        return snapshot.documents.mapNotNull { doc ->
            val review = doc.toObject(ReviewDto::class.java)
            review?.copy(id = doc.id)
        }
    }

    override suspend fun getReviewById(reviewId: String): ReviewDto {
        val docRef = db.collection("reviews").document(reviewId)
        val snapshot = docRef.get().await()

        val review = snapshot.toObject(ReviewDto::class.java)
            ?: throw Exception("La reseña no fue encontrada.")

        return review.copy(id = snapshot.id)
    }

    override suspend fun createReview(review: CreateReviewDto) {
        db.collection("reviews").add(review).await()
    }

    override suspend fun deleteReview(reviewId: String) {
        db.collection("reviews").document(reviewId).delete().await()
    }

    override suspend fun updateReview(reviewId: String, review: CreateReviewDto) {
        db.collection("reviews").document(reviewId).set(review).await()
    }

    override suspend fun getUserReviews(userId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val review = doc.toObject(ReviewDto::class.java)
            review?.copy(id = doc.id)
        }
    }
}