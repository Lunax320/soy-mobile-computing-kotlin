package com.example.soymusicreviewapp.data.repository

import com.example.soymusicreviewapp.data.datasource.remotedatasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthRemoteDataSource
){

    val currentUser: FirebaseUser?
        get() = authDataSource.currentUser
    suspend fun signIn(email: String, password: String): Result<Unit> {
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

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            authDataSource.resetPassword(email)
            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("No existe una cuenta con este correo electrónico"))
        } catch (e: Exception) {
            Result.failure(Exception("Error al enviar el correo. Intenta nuevamente."))
        }
    }
}

