package com.example.soymusicreviewapp.viewmodels

import com.example.soymusicreviewapp.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.datasource.remotedatasource.AuthRemoteDataSource
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.UserRepository
import com.example.soymusicreviewapp.ui.screens.register.RegisterViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegisterViewModelIntegrationTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            //Firebase.firestore.useEmulator("10.0.2.2", 8085)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {}


        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        val userDatasource = UserFirestoreDataSourceImpl(Firebase.firestore)

        userRepository = UserRepository(userDatasource, authRemoteDataSource)
        authRepository = AuthRepository(authRemoteDataSource)
    }


    //ASI PASA
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

        // Act
        viewModel.registerUserOnline()

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

// Pasa, revisar en auth si hay un correo ya creado, si hay, eliminarlo
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun register_alreadyUsedEmail_showsErrorMessage() = runTest {
        // 1. Arrange
        val email = "duplicate@test.com"
        // Prerregistro manual en el emulador para forzar el error
        authRepository.signUp(email, "123456")

        // Usamos UnconfinedTestDispatcher para evitar el deadlock con los .first{}
    // pq se estaba quedando esperando un resultado q no llegaba y se moria por timeout
        val dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        viewModel = RegisterViewModel(authRepository, userRepository, dispatcher)

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange("123456")
        viewModel.onNameChange("Juan")
        viewModel.onUserChange("juanito")

        // 2. Act
        viewModel.onRegisterButtonPressed()

        // NOTA: MAPEADO (Esperamos a que el estado 'loading' pase a true)
        val loadingTrue = viewModel.uiState.map { it.loading }.first { it }
        assertThat(loadingTrue).isTrue()

        // NOTA: MAPEADO (Esperamos a que el proceso termine y 'loading' vuelva a false)
        val loadingFalse = viewModel.uiState.map { it.loading }.first { !it }
        assertThat(loadingFalse).isFalse()

        // 3. Assert
        val state = viewModel.uiState.value
        assertThat(state.navigate).isFalse()
        assertThat(state.errorMessage).isNotEmpty()
        // El mensaje puede variar según el emulador, pero verificamos que falle -- lo retorna en ingles, en español no lo recibe
        assertThat(state.errorMessage).contains("already in use")
    }


    @After
    fun tearDown() = runTest {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                user.delete().await()
            } catch (e: Exception) {
                Firebase.auth.signOut()
            }
        }

    }
}


