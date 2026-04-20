package com.example.soymusicreviewapp.data.datasource.remotedatasource

import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto

interface UserRemoteDataSource {
    suspend fun getUserById(userId: String, currentUserId: String = ""): UserDto
    suspend fun createUser(user: UserDto): UserDto
    suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String): Unit
    suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String): Unit
    suspend fun getFollowingIds(userId: String): List<String>
    suspend fun updateProfileImage(userId: String, imageUrl: String): Unit
}
