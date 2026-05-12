package com.example.soymusicreviewapp.viewModels

import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.ui.screens.foryou.ForYouFeedViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class ForYouFeedViewModelUnitTest {

    private lateinit var viewModel: ForYouFeedViewModel
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var authRepository: AuthRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        reviewRepository = mockk()
        authRepository = mockk()
    }

    // Al cargar reseñas exitosamente, el estado se actualiza con las reseñas
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadReviews_success_updatesStateWithReviews() = runTest {

        // Arrange
        val mockUser = mockk<com.google.firebase.auth.FirebaseUser>(relaxed = true)
        every { mockUser.uid } returns "user_123"
        every { authRepository.currentUser } returns mockUser

        val mockReviews = listOf(
            mockk<Review>(relaxed = true),
            mockk<Review>(relaxed = true)
        )
        coEvery { reviewRepository.getMainReviewsLive() } returns flowOf(mockReviews)

        // Act
        viewModel = ForYouFeedViewModel(reviewRepository, authRepository, UnconfinedTestDispatcher())

        // Assert
        assertThat(viewModel.uiState.value.reviews).hasSize(2)
    }

    // El currentUserId se carga correctamente
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadCurrentUser_updatesCurrentUserId() = runTest {

        // Arrange
        val mockUser = mockk<com.google.firebase.auth.FirebaseUser>(relaxed = true)
        every { mockUser.uid } returns "user_789"
        every { authRepository.currentUser } returns mockUser
        coEvery { reviewRepository.getMainReviewsLive() } returns flowOf(emptyList())

        // Act
        viewModel = ForYouFeedViewModel(reviewRepository, authRepository, UnconfinedTestDispatcher())

        // Assert
        assertThat(viewModel.uiState.value.currentUserId).isEqualTo("user_789")
    }

    // Verifica que el estado inicial del ViewModel tiene isLoading = false
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModel_initialState_isNotLoading() = runTest {

        // Arrange
        val mockUser = mockk<com.google.firebase.auth.FirebaseUser>(relaxed = true)
        every { mockUser.uid } returns "user_123"
        every { authRepository.currentUser } returns mockUser
        coEvery { reviewRepository.getMainReviewsLive() } returns flowOf(emptyList())

        // Act
        viewModel = ForYouFeedViewModel(reviewRepository, authRepository, UnconfinedTestDispatcher())

        // Assert
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }
}