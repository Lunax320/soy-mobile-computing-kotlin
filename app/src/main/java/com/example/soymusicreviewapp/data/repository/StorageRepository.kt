package com.example.soymusicreviewapp.data.repository

import android.net.Uri
import com.example.soymusicreviewapp.data.datasource.remotedatasource.AuthRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.remotedatasource.StorageRemoteDataSource
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storageDataSource: StorageRemoteDataSource,
    private val authDataSource: AuthRemoteDataSource
){

    suspend fun uploadProfileImage(uri: Uri): Result<String> {
        return try {
            val userId = authDataSource.currentUser?.uid
                ?: return Result.failure(Exception("Usuario no autenticado"))

            val path = "profileImages/$userId.jpg"
            val url = storageDataSource.uploadImage(path, uri)

            authDataSource.updateProfileImage(url)

            Result.success(url)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}