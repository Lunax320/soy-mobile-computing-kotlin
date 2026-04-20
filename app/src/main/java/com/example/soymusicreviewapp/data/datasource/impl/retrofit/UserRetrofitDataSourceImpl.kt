package com.example.soymusicreviewapp.data.datasource.impl.retrofit

import com.example.soymusicreviewapp.data.datasource.remotedatasource.UserRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.services.UserRetrofitService
import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getUserById(userId: String, currentUserId: String): UserDto {
        return service.getUserById(userId.toInt())
    }

    override suspend fun createUser(user: UserDto): UserDto {
        return service.createUser(user)
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        //pendiente de implementar
    }

    override suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String) {
        TODO("Not yet implemented")
    }
}
