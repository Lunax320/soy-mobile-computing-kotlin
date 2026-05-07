package com.example.soymusicreviewapp.data.repository

import android.util.Log
import com.example.soymusicreviewapp.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.datasource.remotedatasource.AuthRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserFirestoreDataSourceImpl,
    // private val authRepository: AuthRepository,
    private val authRemoteDataSource: AuthRemoteDataSource

) {
    suspend fun getUserById(userId: String): Result<UserDto> {
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""
        return try {
            val user = remoteDataSource.getUserById(userId, currentUserId)
            if (user == null) return Result.failure(Exception("User not found"))
            Result.success(user)
        } catch (e: Exception) {
            Log.d("UserRepository", "Error obteniendo usuario: " + e.message)
            Result.failure(e)
        }
    }


    suspend fun registerUser(username: String, fullname: String? = null, userId: String): Result<Unit> {
        return try {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            val registerUserDto = RegisterUserDto(username, fullname, fcmToken)
            remoteDataSource.registerUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("TAG", "getUserById: " + e.message)
            Result.failure(e)
        }
    }

    suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String): Result<Unit>{
        return try{
            remoteDataSource.followOrUnfollowUser(currentUserId, targetUserId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("followOrUnfollowUser", "Error: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getFollowingIds(userId: String): List<String> {
        return try {
            remoteDataSource.getFollowingIds(userId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateProfileImage(userId: String, imageUrl: String): Result<Unit> {
        return try {
            remoteDataSource.updateProfileImage(userId, imageUrl)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserInfo(userId: String, name: String, username: String): Result<Unit> {
        return try {
            remoteDataSource.updateUserInfo(userId, name, username)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowers(userId: String): Result<List<UserDto>> {
        return try {
            val followers = remoteDataSource.getFollowers(userId)
            Result.success(followers)
        } catch (e: Exception) {
            Log.d("UserRepository", "Error obteniendo followers: " + e.message)
            Result.failure(e)
        }
    }

    suspend fun getFollowing(userId: String): Result<List<UserDto>> {
        return try {
            val following = remoteDataSource.getFollowing(userId)
            Result.success(following)
        } catch (e: Exception) {
            Log.d("UserRepository", "Error obteniendo following: " + e.message)
            Result.failure(e)
        }
    }
}
