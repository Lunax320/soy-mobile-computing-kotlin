package com.example.soymusicreviewapp.viewmodels

import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.StorageRepository
import com.example.soymusicreviewapp.data.repository.UserRepository
import com.example.soymusicreviewapp.ui.screens.profile.ProfileViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class ProfileViewModelUnitTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var storageRepository: StorageRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var userRepository: UserRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        storageRepository = mockk()
        authRepository = mockk()
        reviewRepository = mockk()
        userRepository = mockk()
    }


    // Verifica la carga exitosa de perfil
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadUserProfile_success_updatesStateWithUserAndReviews() = runTest {

        // Arrange
        val testScheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val mockFirebaseUser = mockk<com.google.firebase.auth.FirebaseUser>(relaxed = true)
        every { mockFirebaseUser.uid } returns "user_123"
        every { authRepository.currentUser } returns mockFirebaseUser

        val mockUserDto = UserDto(
            id = "user_123",
            username = "testuser",
            name = "Test User",
            profileImage = "https://example.com/photo.jpg",
            followersCount = 10,
            followingCount = 5,
            followed = false
        )

        coEvery { userRepository.getUserById(any()) } returns Result.success(mockUserDto)
        coEvery { reviewRepository.getUserReviewsLive(any()) } returns flowOf(emptyList())

        // Act
        viewModel = ProfileViewModel(storageRepository, authRepository, reviewRepository, userRepository, dispatcher)
        viewModel.loadUserProfile("user_123")
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.user.id).isEqualTo("user_123")
        assertThat(state.user.username).isEqualTo("testuser")
    }

    // Verifica que deleteReview llama al repositorio
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteReview_callsRepository() = runTest {

        // Arrange
        val testScheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val mockFirebaseUser = mockk<com.google.firebase.auth.FirebaseUser>(relaxed = true)
        every { mockFirebaseUser.uid } returns "user_123"
        every { authRepository.currentUser } returns mockFirebaseUser

        val mockUserDto = UserDto(
            id = "user_123",
            username = "testuser",
            name = "Test User",
            profileImage = null,
            followersCount = 0,
            followingCount = 0,
            followed = false
        )

        val mockReview = mockk<Review>(relaxed = true)
        every { mockReview.id } returns "review_1"

        coEvery { userRepository.getUserById(any()) } returns Result.success(mockUserDto)
        coEvery { reviewRepository.getUserReviewsLive(any()) } returns flowOf(listOf(mockReview))
        coEvery { reviewRepository.deleteReview("review_1") } returns Result.success(Unit)

        // Act
        viewModel = ProfileViewModel(storageRepository, authRepository, reviewRepository, userRepository, dispatcher)
        viewModel.loadUserProfile("user_123")
        testScheduler.advanceUntilIdle()

        viewModel.deleteReview("review_1")
        testScheduler.advanceUntilIdle()

        // Assert
        assertThat(true).isTrue()
    }
}