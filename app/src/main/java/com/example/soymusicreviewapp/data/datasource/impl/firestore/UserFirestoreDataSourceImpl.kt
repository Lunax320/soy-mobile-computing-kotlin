package com.example.soymusicreviewapp.data.datasource.impl.firestore

import com.example.soymusicreviewapp.data.datasource.remotedatasource.UserRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore): UserRemoteDataSource {
    override suspend fun getUserById(userId: String): UserDto {
        val docRef = db.collection("users").document(userId)
        val respuesta = docRef.get().await()

        if (!respuesta.exists()) {
            throw Exception("El documento del usuario no existe en Firestore")
        }

        val user = respuesta.toObject(UserDto::class.java) ?: throw Exception("No se pudo procesar el JSON del usuario")

        // Mapeamos el ID del documento al objeto de Kotlin
        return user.copy(id = respuesta.id)
    }

    override suspend fun createUser(user: UserDto): UserDto {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.set(registerUserDto).await()
    }

}