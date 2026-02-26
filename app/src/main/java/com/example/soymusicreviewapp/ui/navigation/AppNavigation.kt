package com.example.soymusicreviewapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.soymusicreviewapp.ui.screens.authentication.StartScreen
import com.example.soymusicreviewapp.ui.screens.authentication.LoginScreen
import com.example.soymusicreviewapp.ui.screens.authentication.RegisterScreen
import com.example.soymusicreviewapp.ui.screens.explore.ExploreScreen
import com.example.soymusicreviewapp.ui.screens.create.CreateReviewScreen
import com.example.soymusicreviewapp.ui.screens.feed.ForYouScreen
import com.example.soymusicreviewapp.ui.screens.feed.FollowingFeedScreen
import com.example.soymusicreviewapp.ui.screens.feed.LatestFeedScreen
import com.example.soymusicreviewapp.ui.screens.notifications.NotificationScreen
import com.example.soymusicreviewapp.ui.screens.profile.ProfileScreen

sealed class Screen (val route: String) {
    object StartScreen : Screen("start")
    object LoginScreen : Screen("login")
    object RegisterScreen : Screen("register")
    object ForYouFeedScreen : Screen("forYouFeed")
    object FollowingFeedScreen : Screen("followingFeed")
    object LatestFeedScreen : Screen("latestFeed")
    object ExploreScreen : Screen("explore")
    object CreateReviewScreen : Screen("create")
    object NotificationScreen : Screen("notification")
    object ProfileScreen : Screen("profile")
    object SettingsScreen : Screen("settings")
}

@Composable
fun AppNavigation (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ForYouFeedScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.StartScreen.route) {
            StartScreen()
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen()
        }

        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen()
        }

        composable(route = Screen.ForYouFeedScreen.route) {
            ForYouScreen()
        }

        composable(route = Screen.FollowingFeedScreen.route) {
            FollowingFeedScreen()
        }

        composable(route = Screen.LatestFeedScreen.route) {
            LatestFeedScreen()
        }

        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen()
        }

        composable(route = Screen.CreateReviewScreen.route) {
            CreateReviewScreen()
        }

        composable(route = Screen.NotificationScreen.route) {
            NotificationScreen()
        }

        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen()
        }

        composable(route = Screen.SettingsScreen.route) {
            /*SettingsScreen()*/
        }


    }
}
