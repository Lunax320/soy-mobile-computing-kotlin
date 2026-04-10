package com.example.soymusicreviewapp.data.repository

import com.example.soymusicreviewapp.data.datasource.impl.UserRetrofitDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.UserDto
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRetrofitDataSourceImpl
) {
    suspend fun createUserIfNeeded(user: UserDto): Result<Unit> {
        return try {
            remoteDataSource.createUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}