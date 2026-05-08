package com.example.soymusicreviewapp.e2e

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.soymusicreviewapp.MainActivity
import com.example.soymusicreviewapp.data.dtos.ReviewDto
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
class LoginUserE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val actorEmail = "actor@test.com"
    private val actorPassword = "password123"
    private val targetUserId = "target_user_id"
    private val targetUserName = "Target User"
    private val targetHandle = "targetuser"

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) { }

        runBlocking {
            // Limpieza profunda de colecciones
            val collections = listOf("users", "reviews")
            for (col in collections) {
                val docs = Firebase.firestore.collection(col).get().await()
                for (doc in docs) doc.reference.delete().await()
            }

            // Crear el usuario objetivo
            val targetUser = UserDto(targetUserId, targetHandle, targetUserName, null, 0, 0, false)
            Firebase.firestore.collection("users").document(targetUserId).set(targetUser).await()

            // Crear una reseña para que aparezca en el Home/Social
            val reviewDto = ReviewDto(
                id = "rev_1",
                userId = targetUserId,
                songId = "song_1",
                songName = "Bohemian Rhapsody",
                artistName = "Queen",
                reviewText = "Obra maestra.",
                rating = 5,
                date = "2023-10-27",
                createdAt = "2023-10-27",
                updatedAt = "2023-10-27",
                parentId = null,
                user = targetUser,
                likesCount = 0,
                liked = false
            )
            Firebase.firestore.collection("reviews").document("rev_1").set(reviewDto).await()

            // Asegurar que el actor existe
            val auth = Firebase.auth
            val actorId = try {
                auth.createUserWithEmailAndPassword(actorEmail, actorPassword).await().user?.uid
            } catch (e: Exception) {
                auth.signInWithEmailAndPassword(actorEmail, actorPassword).await().user?.uid
            } ?: "actor_id"

            Firebase.firestore.collection("users").document(actorId).set(
                UserDto(actorId, "actoruser", "Actor", null, 0, 0, false)
            ).await()

            auth.signOut()
        }
    }

    @Test
    fun full_follow_flow_e2e() {
        // 1. Login
        composeRule.onNodeWithTag("btnLogin").performClick()
        composeRule.onNodeWithTag("txtEmailLogin").performTextInput(actorEmail)
        composeRule.onNodeWithTag("txtPasswordLogin").performTextInput(actorPassword)
        composeRule.onNodeWithTag("btnLoginFinal").performClick()

        // Esperar a que el Home cargue
        composeRule.waitUntil(15000) {
            composeRule.onAllNodesWithTag("forYouFeedScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // 2. Navegar al perfil del usuario objetivo
        composeRule.waitUntil(15000) {
            composeRule.onAllNodesWithText(targetHandle).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(targetHandle).performClick()

        // 3. Verificar Información Correcta (esperando a que cargue el perfil)
        composeRule.waitUntil(10000) {
            composeRule.onAllNodesWithTag("profileName").fetchSemanticsNodes().any {
                it.config.getOrNull(SemanticsProperties.Text)?.first()?.text == targetUserName
            }
        }

        composeRule.onNodeWithTag("profileUsername").assertTextEquals(targetHandle)
        composeRule.onNodeWithTag("profileFollowersCount").assertTextEquals("0")

        // Dar Follow
        composeRule.onNodeWithContentDescription("Follow user").performClick()

        // Verificar que el contador de seguidores aumenta a 1
        composeRule.waitUntil(10000) {
            composeRule.onNodeWithTag("profileFollowersCount").fetchSemanticsNode().let {
                it.config.getOrNull(SemanticsProperties.Text)?.first()?.text == "1"
            }
        }

        // 4. Volver al Home e ir a la sección Social
        composeRule.onNodeWithContentDescription("Arrow back").performClick()
        composeRule.onNodeWithTag("tabSocial").performClick()

        // 5. Verificar que aparezca la publicación en Social
        composeRule.waitUntil(15000) {
            composeRule.onAllNodesWithText("Bohemian Rhapsody").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Bohemian Rhapsody").assertIsDisplayed()
        composeRule.onNodeWithText(targetHandle).assertIsDisplayed()
    }

    @After
    fun cleanDatabase() = runBlocking {
        try {
            val currentUser = Firebase.auth.currentUser
            currentUser?.delete()?.await()
        } catch (e: Exception) { }
        Firebase.auth.signOut()
    }
}