package com.example.soymusicreviewapp.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.soymusicreviewapp.MainActivity
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
class RegisterNewUserE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8085)
            //Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) { }

        runBlocking {
            // Limpiar Firestore
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
                val user = Firebase.auth.currentUser
                user?.delete()?.await()
            } catch (e: Exception) { }
            Firebase.auth.signOut()
        }
    }

    @Test
    fun navigate_fromStart_toLogin() {
        composeRule.onNodeWithTag("btnLogin").performClick()
        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("loginScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
    }

    @Test
    fun navigate_fromStart_toRegister() {
        composeRule.onNodeWithTag("btnRegister").performClick()
        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("registerScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("registerScreen").assertIsDisplayed()
    }

    @Test
    fun registerUser_shortPassword_showMessage() {
        composeRule.onNodeWithTag("btnRegister").performClick()
        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("registerScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("txtfullname").performTextInput("Usuario Prueba")
        composeRule.onNodeWithTag("txtuser").performTextInput("pruebausuario")
        composeRule.onNodeWithTag("txtemail").performTextInput("prueba@gmail.com")
        composeRule.onNodeWithTag("txtpassword").performTextInput("1234")
        composeRule.onNodeWithTag("btnRegisterFinal").performClick()

        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("errorMessage").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("errorMessage").assertIsDisplayed()
    }

    @Test
    fun registerUser_usedEmail_showMessage() {
        // Primero crear un usuario con email admin@admin.com
        runBlocking {
            try {
                Firebase.auth.createUserWithEmailAndPassword("admin@admin.com", "123456").await()
            } catch (e: Exception) { }
        }

        composeRule.onNodeWithTag("btnRegister").performClick()
        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("registerScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("txtfullname").performTextInput("Usuario Prueba")
        composeRule.onNodeWithTag("txtuser").performTextInput("pruebausuario")
        composeRule.onNodeWithTag("txtemail").performTextInput("admin@admin.com")
        composeRule.onNodeWithTag("txtpassword").performTextInput("123456")
        composeRule.onNodeWithTag("btnRegisterFinal").performClick()

        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("errorMessage").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("errorMessage").assertIsDisplayed()
    }

    @Test
    fun registerUser_allValidInputs_navigateToHome() {
        composeRule.onNodeWithTag("btnRegister").performClick()
        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("registerScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("txtfullname").performTextInput("Usuario Prueba")
        composeRule.onNodeWithTag("txtuser").performTextInput("pruebausuario")
        composeRule.onNodeWithTag("txtemail").performTextInput("prueba@gmail.com")
        composeRule.onNodeWithTag("txtpassword").performTextInput("123456")
        composeRule.onNodeWithTag("btnRegisterFinal").performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("forYouFeedScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("forYouFeedScreen").assertIsDisplayed()
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