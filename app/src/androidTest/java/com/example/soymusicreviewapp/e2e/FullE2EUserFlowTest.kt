package com.example.soymusicreviewapp.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.soymusicreviewapp.MainActivity
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FullE2EUserFlowTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var testReviewId: String

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8085)
        } catch (e: Exception) { }

        runBlocking {
            // Limpiar Firestore completamente
            val users = Firebase.firestore.collection("users").get().await()
            for (user in users) {
                try {
                    val followers = user.reference.collection("followers").get().await()
                    for (f in followers) f.reference.delete().await()
                    val following = user.reference.collection("following").get().await()
                    for (f in following) f.reference.delete().await()
                } catch (e: Exception) { }
                user.reference.delete().await()
            }

            val reviews = Firebase.firestore.collection("reviews").get().await()
            for (review in reviews) {
                try {
                    val likes = review.reference.collection("likes").get().await()
                    for (l in likes) l.reference.delete().await()
                } catch (e: Exception) { }
                review.reference.delete().await()
            }

            // Limpiar Auth
            try {
                val currentUser = Firebase.auth.currentUser
                currentUser?.delete()?.await()
            } catch (e: Exception) { }
            Firebase.auth.signOut()

            // Crear usuario seed (dueño de la reseña)
            val authResult = Firebase.auth.createUserWithEmailAndPassword("seed@test.com", "123456").await()
            val userId = authResult.user?.uid ?: ""

            val userDto = UserDto(
                id = userId,
                username = "seeduser",
                name = "Seed User",
                profileImage = null,
                followersCount = 0,
                followingCount = 0,
                followed = false
            )

            Firebase.firestore.collection("users").document(userId).set(userDto).await()

            // Crear reseña específica para la prueba
            val testReview = CreateReviewDto(
                userId = userId,
                songId = "1",
                songName = "The Mother We Share",
                artistName = "CHVRCHES",
                reviewText = "Test review for E2E - Esta es la publicación de prueba",
                rating = 5,
                date = "2025-01-01",
                parentId = null,
                user = userDto
            )

            val reviewRef = Firebase.firestore.collection("reviews").add(testReview).await()
            testReviewId = reviewRef.id

            Firebase.auth.signOut()
        }
    }

    // NORMAL (ULTRA RAPIDO NO SE VE NADOTA) USANDO WAITUNTILTAG
    @Test
    fun fullUserFlow_registers_likesAndUnlikesReview() {
        // Navegar a la pantalla de registro
        composeRule.onNodeWithTag("btnRegister").performClick()

        // Esperar a que la pantalla de registro sea visible
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("registerScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Intentar registro con contrasena corta 1234
        composeRule.onNodeWithTag("txtfullname").performTextInput("Usuario Prueba")
        composeRule.onNodeWithTag("txtuser").performTextInput("pruebausuario")
        composeRule.onNodeWithTag("txtemail").performTextInput("prueba@gmail.com")
        composeRule.onNodeWithTag("txtpassword").performTextInput("1234")
        composeRule.onNodeWithTag("btnRegisterFinal").performClick()

        // Esperar a que aparezca el mensaje de error
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("errorMessage").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("errorMessage").assertIsDisplayed()

        // Corregir contrasena 123456)
        composeRule.onNodeWithTag("txtpassword").performTextInput("123456")
        composeRule.onNodeWithTag("btnRegisterFinal").performClick()

        // Esperar navegacion a ForYou con tiempo extendido para ver el cambio
        composeRule.waitUntil(timeoutMillis = 15000) {
            composeRule.onAllNodesWithTag("forYouFeedScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Buscar y entrar a la publicacion de prueba creada en el setup
        composeRule.waitUntil(timeoutMillis = 8000) {
            composeRule.onAllNodesWithTag("reviewCard_$testReviewId").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("reviewCard_$testReviewId").performClick()

        // Verificar que la pantalla de detalle cargo
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewDetailScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Probar boton de retroceso en el detalle
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("backButton").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("backButton").performClick()

        // Esperar a estar de vuelta en el feed
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("forYouFeedScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Dar like a la publicacion desde la tarjeta del feed
        composeRule.onNodeWithTag("likeButton").performClick()

        // Espera forzada de sincronizacion para asegurar que Firebase registro el like
        composeRule.mainClock.advanceTimeByFrame() // Procesa el frame actual
        composeRule.waitUntil(timeoutMillis = 5000) {
            // Se puede usar un delay interno de Compose sin bloquear el hilo
            composeRule.mainClock.advanceTimeBy(1000)
            true
        }

        // Entrar de nuevo al detalle para verificar persistencia del like
        composeRule.onNodeWithTag("reviewCard_$testReviewId").performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewDetailScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Volver al feed
        composeRule.onNodeWithTag("backButton").performClick()

        // Esperar carga del feed
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("forYouFeedScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Quitar el like desde el feed
        composeRule.onNodeWithTag("likeButton").performClick()

        // Espera de sincronizacion para que el estado de 'no like' se guarde bien
        composeRule.mainClock.advanceTimeByFrame()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.mainClock.advanceTimeBy(1000)
            true
        }

        // Entrar por ultima vez para verificar que ya no tiene like
        composeRule.onNodeWithTag("reviewCard_$testReviewId").performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewDetailScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Finalizar volviendo a la pantalla principal
        composeRule.onNodeWithTag("backButton").performClick()

        // Pequena espera final para que la UI se actualice antes de cerrar
        composeRule.mainClock.advanceTimeBy(500)
    }



    /*
    // USANDO THREAD PARA VER EL PASITO A PASITO, ES EXACTAMENTE LA MISMA DE ARRIBA
    // SOLO QUE USANDO PUES THREAD

    @Test
    fun fullUserFlow_registers_likesAndUnlikesReview() {
        // Navegar a registro
        composeRule.onNodeWithTag("btnRegister").performClick()
        Thread.sleep(1500)

        // Llenar formulario con contraseña corta 1234
        composeRule.onNodeWithTag("txtfullname").performTextInput("Usuario Prueba")
        Thread.sleep(500)
        composeRule.onNodeWithTag("txtuser").performTextInput("pruebausuario")
        Thread.sleep(500)
        composeRule.onNodeWithTag("txtemail").performTextInput("prueba@gmail.com")
        Thread.sleep(500)
        composeRule.onNodeWithTag("txtpassword").performTextInput("1234")
        Thread.sleep(500)
        composeRule.onNodeWithTag("btnRegisterFinal").performClick()

        // Verificar mensaje de error por contraseña corta
        Thread.sleep(2000)
        composeRule.onNodeWithTag("errorMessage").assertIsDisplayed()

        // Corregir contraseña 123456 y registrar
        composeRule.onNodeWithTag("txtpassword").performTextInput("123456")
        Thread.sleep(500)
        composeRule.onNodeWithTag("btnRegisterFinal").performClick()

        // Esperar navegacion a la pantalla principal
        Thread.sleep(5000)
        composeRule.onNodeWithTag("forYouFeedScreen").assertIsDisplayed()

        // Entrar a la primera publicacion
        Thread.sleep(2000)
        composeRule.onNodeWithTag("reviewCard_$testReviewId").performClick()

        // Verificar que se cargo el detalle
        Thread.sleep(3000)
        composeRule.onNodeWithTag("reviewDetailScreen").assertIsDisplayed()

        // Volver atras
        Thread.sleep(2000)
        composeRule.onNodeWithTag("backButton").performClick()

        // Esperar volver al feed
        Thread.sleep(2000)

        // Dar like desde el feed
        Thread.sleep(1500)
        composeRule.onNodeWithTag("likeButton").performClick()

        // Esperar a que el like se procese
        Thread.sleep(3000)

        // Volver a entrar a la misma publicación
        Thread.sleep(2000)
        composeRule.onNodeWithTag("reviewCard_$testReviewId").performClick()

        // Verificar que se ve el detalle (ya con like)
        Thread.sleep(3000)
        composeRule.onNodeWithTag("reviewDetailScreen").assertIsDisplayed()

        // Volver atras
        Thread.sleep(2000)
        composeRule.onNodeWithTag("backButton").performClick()

        Thread.sleep(2000)

        // Quitar el like desde el feed
        Thread.sleep(1500)
        composeRule.onNodeWithTag("likeButton").performClick()

        Thread.sleep(3000)

        // Volver a entrar para verificar
        Thread.sleep(2000)
        composeRule.onNodeWithTag("reviewCard_$testReviewId").performClick()

        Thread.sleep(3000)
        composeRule.onNodeWithTag("reviewDetailScreen").assertIsDisplayed()

        // Volver atras para terminar
        Thread.sleep(2000)
        composeRule.onNodeWithTag("backButton").performClick()

        // Pausa final (sin verificar feed al final)
        Thread.sleep(1500)
    }
    */


    @After
    fun cleanDatabase() = runBlocking {
        try {
            val currentUser = Firebase.auth.currentUser
            currentUser?.delete()?.await()
        } catch (e: Exception) { }

        try {
            val seedUser = Firebase.auth.signInWithEmailAndPassword("seed@test.com", "123456").await()
            seedUser.user?.delete()?.await()
        } catch (e: Exception) { }

        Firebase.auth.signOut()

        // Limpiar Firestore
        try {
            val users = Firebase.firestore.collection("users").get().await()
            for (user in users) {
                user.reference.delete().await()
            }
            val reviews = Firebase.firestore.collection("reviews").get().await()
            for (review in reviews) {
                review.reference.delete().await()
            }
        } catch (e: Exception) { }
    }
}