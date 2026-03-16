package com.example.soymusicreviewapp.data.repository

import com.example.soymusicreviewapp.data.datasource.AuthRemoteDataSource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthRemoteDataSource
){

    val currentUser = authDataSource.currentUser

    suspend fun singIn(email: String, password: String): Result<Unit> {
        try {
            authDataSource.singIn(email, password)
            return Result.success(Unit)
        }
        catch (e: FirebaseAuthInvalidCredentialsException) {
            return Result.failure(Exception("Credenciales incorrectas"))
        }
        catch (e: FirebaseAuthInvalidUserException){
            return Result.failure(Exception("Usuario no valido"))
        }
        catch (e: Exception) {
            return Result.failure(Exception("Error al iniciar sesion"))
        }
    }

    suspend fun signUp(email: String, password: String): Result<Unit> {
        try{
            authDataSource.signUp(email, password)
            return Result.success(Unit)
        }
        catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun signOut() {
        authDataSource.signOut()
    }

}