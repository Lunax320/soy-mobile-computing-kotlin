package com.example.soymusicreviewapp.data.datasource.impl.firestore

import com.example.soymusicreviewapp.data.datasource.remotedatasource.UserRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore): UserRemoteDataSource {

    override suspend fun getUserById(id: String, currentUserId: String): UserDto {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()
        val user = respuesta.toObject(UserDto::class.java) ?: throw Exception("No se pudo obtener el usuario")

        val followerDoc = db.collection("users").document(id)
            .collection("followers").document(currentUserId).get().await()

        val exist = followerDoc.exists()

        user.followed = exist
        return user
    }

    override suspend fun createUser(user: UserDto): UserDto {
        TODO("Not yet implemented")
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
}
