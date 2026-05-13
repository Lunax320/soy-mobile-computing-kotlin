package com.example.soymusicreviewapp.data.datasource.impl.firestore

import com.example.soymusicreviewapp.data.datasource.remotedatasource.ReviewRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.ReviewDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewRemoteDataSource {

    override suspend fun getAllReviews(currentUserId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews").get().await()

        return snapshot.documents.map { doc ->
            val review = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
            var finalReview = review.copy(id = doc.id)
            
            if (currentUserId.isNotEmpty()) {
                val likeDoc = db.collection("reviews").document(doc.id)
                    .collection("likes").document(currentUserId).get().await()
                if (likeDoc.exists()) {
                    finalReview = finalReview.copy(liked = true)
                }
            }
            finalReview
        }
    }

    override suspend fun getReviewById(reviewId: String, currentuserId: String): ReviewDto {
        val reviewRef = db.collection("reviews").document(reviewId)
        val reviewSnapshot = reviewRef.get().await()
        val review = reviewSnapshot.toObject(ReviewDto::class.java) ?: throw Exception("Review not found")
        
        var finalReview = review.copy(id = reviewSnapshot.id)

        if (currentuserId.isNotEmpty()) {
            val likeSnapshot = reviewRef.collection("likes").document(currentuserId).get().await()
            if (likeSnapshot.exists()) {
                finalReview = finalReview.copy(liked = true)
            }
        }
        return finalReview
    }

    /*override*/ suspend fun getReviewReplies(reviewId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews").document(reviewId)
            .collection("comments").get().await()

        return snapshot.documents.map { doc ->
            val review = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
            review.copy(id = doc.id)
        }
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

    override suspend fun getUserReviews(userId: String, currentUserId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            val review = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
            var finalReview = review.copy(id = doc.id)
            
            if (currentUserId.isNotEmpty()) {
                val likeDoc = db.collection("reviews").document(doc.id)
                    .collection("likes").document(currentUserId).get().await()
                if (likeDoc.exists()) {
                    finalReview = finalReview.copy(liked = true)
                }
            }
            finalReview
        }
    }

    override suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        val reviewRef = db.collection("reviews").document(reviewId)
        val likesRef = reviewRef.collection("likes").document(userId)

        db.runTransaction { transaction ->
            val likeDoc = transaction.get(likesRef)

            if (likeDoc.exists()) {
                transaction.delete(likesRef)
                transaction.update(reviewRef, "likesCount", FieldValue.increment(-1))
            } else {
                transaction.set(likesRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(reviewRef, "likesCount", FieldValue.increment(1))
            }
        }.await()
    }

    override fun listenAllReviews(currentUserId: String): Flow<List<ReviewDto>> = callbackFlow {
        val listener = db.collection("reviews").addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                launch {
                    val reviews = snapshot.documents.map { doc ->
                        val review = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
                        var finalReview = review.copy(id = doc.id)
                        
                        if (currentUserId.isNotEmpty()) {
                            val hasLiked = db.collection("reviews").document(doc.id)
                                .collection("likes").document(currentUserId).get().await().exists()
                            finalReview = finalReview.copy(liked = hasLiked)
                        }
                        finalReview
                    }
                    trySend(reviews)
                }
            }
        }
        awaitClose { listener.remove() }
    }

    override fun listenUserReviews(userId: String, currentUserId: String): Flow<List<ReviewDto>> = callbackFlow {
        val listener = db.collection("reviews")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    launch {
                        val reviews = snapshot.documents.map { doc ->
                            val review = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
                            var finalReview = review.copy(id = doc.id)
                            
                            if (currentUserId.isNotEmpty()) {
                                val hasLiked = db.collection("reviews").document(doc.id)
                                    .collection("likes").document(currentUserId).get().await().exists()
                                finalReview = finalReview.copy(liked = hasLiked)
                            }
                            finalReview
                        }
                        trySend(reviews)
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override fun listenSongReviews(songId: String, currentUserId: String): Flow<List<ReviewDto>> = callbackFlow {
        val listener = db.collection("reviews")
            .whereEqualTo("songId", songId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    launch {
                        val reviews = snapshot.documents.map { doc ->
                            val review = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
                            var finalReview = review.copy(id = doc.id)
                            
                            if (currentUserId.isNotEmpty()) {
                                val hasLiked = db.collection("reviews").document(doc.id)
                                    .collection("likes").document(currentUserId).get().await().exists()
                                finalReview = finalReview.copy(liked = hasLiked)
                            }
                            finalReview
                        }
                        trySend(reviews)
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getCommentsForReview(parentReviewId: String, currentUserId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("parentId", parentReviewId)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            val review = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
            var finalReview = review.copy(id = doc.id)

            if (currentUserId.isNotEmpty()) {
                val likeDoc = db.collection("reviews").document(doc.id)
                    .collection("likes").document(currentUserId).get().await()
                if (likeDoc.exists()) {
                    finalReview = finalReview.copy(liked = true)
                }
            }
            finalReview
        }
    }

    override fun listenCommentsForReview(parentReviewId: String, currentUserId: String): Flow<List<ReviewDto>> = callbackFlow {
        val listener = db.collection("reviews")
            .whereEqualTo("parentId", parentReviewId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    launch {
                        val comments = snapshot.documents.map { doc ->
                            val review = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
                            var finalReview = review.copy(id = doc.id)

                            if (currentUserId.isNotEmpty()) {
                                val hasLiked = db.collection("reviews").document(doc.id)
                                    .collection("likes").document(currentUserId).get().await().exists()
                                finalReview = finalReview.copy(liked = hasLiked)
                            }
                            finalReview
                        }
                        trySend(comments)
                    }
                }
            }
        awaitClose { listener.remove() }
    }
    
    override suspend fun getLast24ReviewsWithLocation(): List<ReviewDto> {
        // Calculamos el tiempo de hace 24 horas en milisegundos
        val twentyFourHoursAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
        val threshold = twentyFourHoursAgo.toString()

        // Consultamos reviews cuya fecha sea mayor al umbral (más recientes)
        // Nota: Al usar un filtro de desigualdad (>), Firestore requiere ordenar por ese mismo campo
        val snapshot = db.collection("reviews")
            .whereGreaterThan("date", threshold)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()

        // Filtramos en memoria las que tienen ubicación (porque Firestore no permite desigualdades en dos campos distintos)
        return snapshot.documents.mapNotNull { doc ->
            val review = doc.toObject(ReviewDto::class.java)
            if (review != null && review.latitude != null && review.longitude != null) {
                review.copy(id = doc.id)
            } else {
                null
            }
        }
    }
}
