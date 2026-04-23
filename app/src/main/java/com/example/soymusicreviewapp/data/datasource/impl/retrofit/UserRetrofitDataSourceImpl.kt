package com.example.soymusicreviewapp.data.datasource.impl.retrofit

import com.example.soymusicreviewapp.data.datasource.remotedatasource.UserRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.services.UserRetrofitService
import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService,
    private val db: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun getUserById(userId: String, currentUserId: String): UserDto {
        // Obtenemos los datos básicos de Retrofit
        val userDto = service.getUserById(userId.toInt())
        
        // Pero el estado de "followed" y otros contadores viven en Firestore
        val followerDoc = db.collection("users").document(userId)
            .collection("followers").document(currentUserId).get().await()

        return userDto.copy(
            id = userId,
            followed = followerDoc.exists()
        )
    }

    override suspend fun createUser(user: UserDto): UserDto {
        return service.createUser(user)
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.set(registerUserDto).await()
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
        db.collection("users").document(userId).update("profileImage", imageUrl).await()
    }

    override suspend fun getFollowers(userId: String): List<UserDto> {
        val snapshot = db.collection("users").document(userId)
            .collection("followers").get().await()
        val followerIds = snapshot.documents.map { it.id }

        return if (followerIds.isEmpty()) emptyList() else {
            // Aquí puedes decidir si traer la info de Firestore o de Retrofit por cada ID.
            // Dado que Firestore tiene la lista de IDs, buscaremos los detalles.
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
}
