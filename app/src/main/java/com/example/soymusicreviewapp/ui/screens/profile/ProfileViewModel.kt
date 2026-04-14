package com.example.soymusicreviewapp.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.StorageRepository
import com.example.soymusicreviewapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    fun loadUserProfile(userId: String) {
        // LOG 1: ¿Está llegando el ID correctamente desde la navegación?
        Log.d("PROFILE_TRACKER", "1. Iniciando carga de perfil. ID recibido: '$userId'")

        viewModelScope.launch {

            // --- RASTREO DEL USUARIO ---
            val userResult = userRepository.getUserById(userId)

            if (userResult.isSuccess) {
                val userProfile = userResult.getOrNull()
                // LOG 2: ¿Encontró el documento en la colección 'users'?
                Log.d("PROFILE_TRACKER", "2. Éxito al buscar usuario. Datos crudos: $userProfile")

                if (userProfile != null) {
                    _uiState.update { state ->
                        state.copy(
                            name = userProfile.name,
                            // Si el username viene vacío de Firebase, aquí se notará
                            username = if (userProfile.username.isNotEmpty()) "@" + userProfile.username.lowercase().replace(" ", "") else "@usuario",
                            profileImageUrl = userProfile.profileImage,
                            followersCount = userProfile.followersCount,
                            followingCount = userProfile.followingCount
                        )
                    }
                    Log.d("PROFILE_TRACKER", "3. Estado UI actualizado con la información del usuario.")
                } else {
                    Log.e("PROFILE_TRACKER", "2. ERROR: El resultado fue exitoso pero el objeto userProfile es NULO.")
                }
            } else {
                // LOG 2.1: Si falló, ¿por qué falló?
                Log.e("PROFILE_TRACKER", "2. ERROR AL BUSCAR USUARIO: ${userResult.exceptionOrNull()?.message}")
            }

            // --- RASTREO DE LAS RESEÑAS ---
            Log.d("PROFILE_TRACKER", "4. Buscando reseñas para el usuario: '$userId'")
            val reviewsResult = reviewRepository.getUserReviews(userId)

            if (reviewsResult.isSuccess) {
                val reviews = reviewsResult.getOrNull()
                // LOG 5: ¿Cuántas reseñas trajo la consulta whereEqualTo?
                Log.d("PROFILE_TRACKER", "5. Éxito al buscar reseñas. Cantidad encontrada: ${reviews?.size}")

                if (reviews != null) {
                    _uiState.update { state ->
                        state.copy(
                            userReviews = reviews,
                            reviewCount = reviews.size
                        )
                    }
                    Log.d("PROFILE_TRACKER", "6. Estado UI actualizado con la lista de reseñas.")
                }
            } else {
                // LOG 5.1: Si la consulta a Firestore falló
                Log.e("PROFILE_TRACKER", "5. ERROR AL BUSCAR RESEÑAS: ${reviewsResult.exceptionOrNull()?.message}")
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            val result = reviewRepository.deleteReview(reviewId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(userReviews = state.userReviews.filter { it.usernameId != reviewId })
                }
            }
        }
    }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess) {
                val imageUrl = result.getOrNull()
                if (imageUrl != null) {
                    _uiState.update { state ->
                        state.copy(profileImageUrl = imageUrl)
                    }
                }
            }
        }
    }
}