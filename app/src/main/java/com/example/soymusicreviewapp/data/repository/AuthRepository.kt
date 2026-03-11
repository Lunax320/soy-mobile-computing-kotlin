package com.example.soymusicreviewapp.data.repository

import com.example.soymusicreviewapp.data.datasource.AuthRemoteDataSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthRemoteDataSource
){

    val currentUser = authDataSource.currentUser

    suspend fun singIn(email: String, password: String) {
        authDataSource.singIn(email, password)
    }

    suspend fun signUp(email: String, password: String) {
        authDataSource.signUp(email, password)
    }

    fun signOut() {
        authDataSource.signOut()
    }

}