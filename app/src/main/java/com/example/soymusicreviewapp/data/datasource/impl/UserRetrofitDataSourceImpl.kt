package com.example.soymusicreviewapp.data.datasource.impl

import com.example.soymusicreviewapp.data.datasource.UserRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.services.UserRetrofitService
import com.example.soymusicreviewapp.data.dtos.UserDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getUserById(userId: String): UserDto {
        return service.getUserById(userId.toInt())
    }

    override suspend fun createUser(user: UserDto): UserDto {
        return service.createUser(user)
    }
}