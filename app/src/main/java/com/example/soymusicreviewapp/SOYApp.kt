package com.example.soymusicreviewapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.soymusicreviewapp.ui.navigation.AppNavigation
import com.example.soymusicreviewapp.ui.navigation.SOYBottomNavigationBar
import com.example.soymusicreviewapp.ui.navigation.Screen
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SOYApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    val showBar = currentRoute != Screen.StartScreen.route &&
            currentRoute != Screen.LoginScreen.route &&
            currentRoute != Screen.RegisterScreen.route &&
            currentRoute != Screen.SplashScreen.route

    Scaffold(
        bottomBar = {
            if (showBar) {
                SOYBottomNavigationBar(
                    navController = navController
                )
            }
        }
    ) { paddingValues ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}