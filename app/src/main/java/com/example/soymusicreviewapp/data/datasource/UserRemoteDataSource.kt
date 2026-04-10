package com.example.soymusicreviewapp.data.datasource

import com.example.soymusicreviewapp.data.dtos.UserDto

interface UserRemoteDataSource {
    suspend fun getUserById(userId: String): UserDto
    suspend fun createUser(user: UserDto): UserDto
}