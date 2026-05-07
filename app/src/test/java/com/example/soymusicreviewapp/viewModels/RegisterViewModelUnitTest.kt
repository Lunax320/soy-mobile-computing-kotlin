package com.example.soymusicreviewapp.viewModels

import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.UserRepository
import com.example.soymusicreviewapp.ui.screens.register.RegisterViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class RegisterViewModelUnitTest {

    private lateinit var viewModel: RegisterViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        authRepository = mockk()
        userRepository = mockk()
        viewModel = RegisterViewModel(authRepository, userRepository)
    }

    //ASI PASA
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun register_success_createsUserAndUpdatesUi() = runTest {
        val testScheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(testScheduler)

        Dispatchers.setMain(dispatcher)
        viewModel = RegisterViewModel(authRepository, userRepository, dispatcher)

        // Arrange
        //Para evitar el error que se queda pegado lo que se creo antes asi se elimine,
        // se le da un ""id"" al email que es el tiempo de creación y asi nunca puede repetirse
        val timeStamp = System.currentTimeMillis()
        viewModel.onEmailChange("test$timeStamp@test.com")

        viewModel.onPasswordChange("123456")
        viewModel.onNameChange("Juan1")
        viewModel.onUserChange("juanito1")

        coEvery { authRepository.signUp(any(), any()) } returns Result.success(Unit)

        coEvery { authRepository.currentUser?.uid } returns "1"

        coEvery {
            userRepository.registerUser(
                any(),
                any(),
                any()
            )
        } returns Result.success(Unit)


        // Act
        viewModel.registerUserOnline()
        //esperar a que todo acabe
        advanceUntilIdle()

        // se usa para que todas las corrutinas se acaben y no queden en segundo plano molestando nuevos procesos
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        // es como un mini flag para saber el error que tenia.
        if (!state.navigate) println("DEBUG: El error fue: ${state.errorMessage}")

        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
        assertThat(state.showMessage).isFalse()
    }

    //----------------------------------------------------------------------------------------------

    // Pasa, revisar en auth si hay un correo ya creado, si hay, eliminarlo
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun register_alreadyUsedEmail_showsErrorMessage() = runTest {
        // 1. Arrange
        val email = "duplicate@test.com"

        // Usamos UnconfinedTestDispatcher para evitar el deadlock con los .first{}
        // pq se estaba quedando esperando un resultado q no llegaba y se moria por timeout
        val dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        viewModel = RegisterViewModel(authRepository, userRepository, dispatcher)

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange("123456")
        viewModel.onNameChange("Juan")
        viewModel.onUserChange("juanito")

        coEvery { authRepository.signUp(any(), any()) } returns Result.failure(Exception("El correo ya esta en uso"))

        // 2. Act
        viewModel.onRegisterButtonPressed()
        advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        assertThat(state.navigate).isFalse()
        assertThat(state.errorMessage).isNotEmpty()
    }


    // Verifica que el registro con todos los campos vacios, muestra un mensaje de error y no navega
    // a la siguiente pantalla
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun register_emptyFields_showsErrorMessage() = runTest {

        // Arrange
        val testScheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(testScheduler)

        Dispatchers.setMain(dispatcher)

        // Crear mocks (aunque no se usen porque la validación falla antes)
        authRepository = mockk()
        userRepository = mockk()

        viewModel = RegisterViewModel(authRepository, userRepository, dispatcher)

        // Todos los campos estan vacíos
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("")
        viewModel.onNameChange("")
        viewModel.onUserChange("")

        // Act
        viewModel.onRegisterButtonPressed()
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.navigate).isFalse()        // No debe navegar
        assertThat(state.showMessage).isTrue()      // Debe mostrar mensaje
        assertThat(state.errorMessage).isEqualTo("Todos los campos son necesarios")
    }
}

