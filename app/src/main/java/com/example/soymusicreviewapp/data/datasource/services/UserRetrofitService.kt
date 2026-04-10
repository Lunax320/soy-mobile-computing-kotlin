package com.example.soymusicreviewapp.data.datasource.services

import com.example.soymusicreviewapp.data.dtos.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserRetrofitService {
    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): UserDto

    @POST("users")
    suspend fun createUser(@Body user: UserDto): UserDto
}