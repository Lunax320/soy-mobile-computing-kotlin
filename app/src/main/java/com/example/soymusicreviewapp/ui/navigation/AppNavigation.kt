package com.example.soymusicreviewapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.soymusicreviewapp.ui.screens.authentication.StartScreen
import com.example.soymusicreviewapp.ui.screens.login.LoginScreen
import com.example.soymusicreviewapp.ui.screens.authentication.RegisterScreen
import com.example.soymusicreviewapp.ui.screens.explore.ExploreScreen
import com.example.soymusicreviewapp.ui.screens.create.CreateReviewScreen
import com.example.soymusicreviewapp.ui.screens.songdetail.SongsDetailScreen
import com.example.soymusicreviewapp.ui.screens.foryou.ForYouFeedScreen
import com.example.soymusicreviewapp.ui.screens.following.FollowingFeedScreen
import com.example.soymusicreviewapp.ui.screens.latest.LatestFeedScreen
import com.example.soymusicreviewapp.ui.screens.reviewdetail.ReviewDetailScreen
import com.example.soymusicreviewapp.ui.screens.notifications.NotificationScreen
import com.example.soymusicreviewapp.ui.screens.profile.ProfileScreen
import com.example.soymusicreviewapp.ui.screens.profile.SettingsScreen

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
        startDestination = Screen.StartScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.StartScreen.route) {
            StartScreen(
                loginButtonPressed = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                registerButtonPressed = {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            )
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                loginButtonPressed = {
                    navController.navigate(Screen.ForYouFeedScreen.route)
                }
            )
        }

        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(
                loginCreateAccount = {
                    navController.navigate(Screen.ForYouFeedScreen.route)
                }
            )
        }

        composable(route = Screen.ForYouFeedScreen.route) {
            ForYouFeedScreen(
                onReviewClick = { reviewId ->
                    navController.navigate("reviewDetail/$reviewId")
                },
                followingButtonPressed = {
                    navController.navigate(Screen.FollowingFeedScreen.route)
                }
            )
        }

        composable(
            route = "reviewDetail/{reviewId}",
            arguments = listOf(navArgument("reviewId") { type = NavType.IntType })
        ) { backStackEntry ->
            val reviewId = backStackEntry.arguments?.getInt("reviewId") ?: 0
            ReviewDetailScreen(
                reviewId = reviewId,
                modifier = Modifier.padding(12.dp),
            )
        }

        composable(route = Screen.FollowingFeedScreen.route) {
            FollowingFeedScreen(
                onReviewClick = { reviewId ->
                    navController.navigate("reviewDetail/$reviewId")
                },
                latestButtonPressed = {
                    navController.navigate(Screen.LatestFeedScreen.route)
                }
            )
        }

        composable(route = Screen.LatestFeedScreen.route) {
            LatestFeedScreen(
                modifier = Modifier,
                latestCreateAccount = {
                    navController.navigate(Screen.ForYouFeedScreen.route)
                }
            )
        }

        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen(
                onSongClick = { songId ->
                    navController.navigate("songDetail/$songId")
                }
            )
        }

        composable(
            route = "songDetail/{songId}",
            arguments = listOf(navArgument("songId") { type = NavType.IntType })
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getInt("songId") ?: 0
            SongsDetailScreen(
                songId = songId,
                onBack = { navController.popBackStack() },
                onReviewClick = { reviewId ->
                    navController.navigate("reviewDetail/$reviewId")
                }
            )
        }

        composable(route = Screen.CreateReviewScreen.route) {
            CreateReviewScreen(
                onSongClick = { songId ->
                    navController.navigate("songDetail/$songId")
                }
            )
        }

        composable(route = Screen.NotificationScreen.route) {
            NotificationScreen()
        }

        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(
                settingsButtonPressed = {
                    navController.navigate(Screen.SettingsScreen.route)
                }
            )
        }

        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(
                onBackClick = {
                    navController.navigate(Screen.ProfileScreen.route)
                }
            )
        }


    }
}
