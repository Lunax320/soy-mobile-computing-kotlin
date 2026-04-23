package com.example.soymusicreviewapp.data.repository

import android.util.Log
import com.example.soymusicreviewapp.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserFirestoreDataSourceImpl,
    private val authRepository: AuthRepository
) {
    suspend fun getUserById(userId: String): Result<UserDto> {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        return try {
            val user = remoteDataSource.getUserById(userId, currentUserId)
            Result.success(user)
        } catch (e: Exception) {
            Log.d("UserRepository", "Error obteniendo usuario: " + e.message)
            Result.failure(e)
        }
    }

    suspend fun createUser(user: UserDto): Result<Unit> {
        return try {
            remoteDataSource.createUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUser(username: String, fullname: String? = null, userId: String): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(username, fullname)
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
}
