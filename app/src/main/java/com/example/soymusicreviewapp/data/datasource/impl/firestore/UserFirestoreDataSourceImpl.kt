package com.example.soymusicreviewapp.data.datasource.impl.firestore

import com.example.soymusicreviewapp.data.datasource.remotedatasource.UserRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore): UserRemoteDataSource {

    override suspend fun getUserById(id: String, currentUserId: String): UserDto? {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()
        val user = respuesta.toObject(UserDto::class.java) ?: return null

        var followed = false
        if (currentUserId.isNotEmpty()) {
            try {
                val followerDoc = db.collection("users").document(id)
                    .collection("followers").document(currentUserId).get().await()
                followed = followerDoc.exists()
            } catch (e: Exception) {
                followed = false
            }
        }

        return user.copy(
            id = respuesta.id,
            followed = followed
        )
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        val user = UserDto(
            id = userId,
            username = registerUserDto.username,
            name = registerUserDto.name ?: registerUserDto.username,
            profileImage = null,
            followersCount = 0,
            followingCount = 0,
            followed = false
        )
        db.collection("users").document(userId).set(user).await()
    }

    override suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String) {
        val currentUserRef = db.collection("users").document(currentUserId)
        val targetUserRef = db.collection("users").document(targetUserId)

        val followingRef = currentUserRef.collection("following").document(targetUserId)
        val followersRef = targetUserRef.collection("followers").document(currentUserId)

        db.runTransaction { transaction ->
            val followingDoc = transaction.get(followingRef)

            if (followingDoc.exists()) {
                transaction.delete(followingRef)
                transaction.delete(followersRef)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(-1))
            } else {
                val timestamp = FieldValue.serverTimestamp()
                transaction.set(followingRef, mapOf("timestamp" to timestamp))
                transaction.set(followersRef, mapOf("timestamp" to timestamp))
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(1))
            }
        }.await()
    }

    override suspend fun getFollowingIds(userId: String): List<String> {
        val snapshot = db.collection("users").document(userId)
            .collection("following").get().await()
        return snapshot.documents.map { it.id }
    }

    override suspend fun updateProfileImage(userId: String, imageUrl: String) {
        // ASEGURAMOS QUE EL NOMBRE DEL CAMPO COINCIDA CON UserDto (profileImage)
        db.collection("users").document(userId).update("profileImage", imageUrl).await()
    }

    override suspend fun getFollowers(userId: String): List<UserDto> {
        val snapshot = db.collection("users").document(userId)
            .collection("followers").get().await()
        val followerIds = snapshot.documents.map { it.id }

        return if (followerIds.isEmpty()) emptyList() else {
            val usersSnapshot = db.collection("users")
                .whereIn("__name__", followerIds)
                .get().await()
            usersSnapshot.documents.mapNotNull { it.toObject(UserDto::class.java)?.copy(id = it.id) }
        }
    }

    override suspend fun getFollowing(userId: String): List<UserDto> {
        val snapshot = db.collection("users").document(userId)
            .collection("following").get().await()
        val followingIds = snapshot.documents.map { it.id }

        return if (followingIds.isEmpty()) emptyList() else {
            val usersSnapshot = db.collection("users")
                .whereIn("__name__", followingIds)
                .get().await()
            usersSnapshot.documents.mapNotNull { it.toObject(UserDto::class.java)?.copy(id = it.id) }
        }
    }

    suspend fun updateUserInfo(userId: String, name: String, username: String) {
        db.collection("users").document(userId)
            .update(
                mapOf(
                    "name" to name,
                    "username" to username
                )
            ).await()
    }
}
