package com.example.soymusicreviewapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.soymusicreviewapp.ui.screens.start.StartScreen
import com.example.soymusicreviewapp.ui.screens.login.LoginScreen
import com.example.soymusicreviewapp.ui.screens.register.RegisterScreen
import com.example.soymusicreviewapp.ui.screens.explore.ExploreScreen
import com.example.soymusicreviewapp.ui.screens.create.CreateReviewScreen
import com.example.soymusicreviewapp.ui.screens.create.CreateReviewViewModel
import com.example.soymusicreviewapp.ui.screens.explore.ExploreViewModel
import com.example.soymusicreviewapp.ui.screens.songdetail.SongsDetailScreen
import com.example.soymusicreviewapp.ui.screens.foryou.ForYouFeedScreen
import com.example.soymusicreviewapp.ui.screens.following.FollowingFeedScreen
import com.example.soymusicreviewapp.ui.screens.following.FollowingFeedViewModel
import com.example.soymusicreviewapp.ui.screens.foryou.ForYouFeedViewModel
import com.example.soymusicreviewapp.ui.screens.latest.LatestFeedScreen
import com.example.soymusicreviewapp.ui.screens.latest.LatestFeedViewModel
import com.example.soymusicreviewapp.ui.screens.login.LoginViewModel
import com.example.soymusicreviewapp.ui.screens.reviewdetail.ReviewDetailScreen
import com.example.soymusicreviewapp.ui.screens.notifications.NotificationScreen
import com.example.soymusicreviewapp.ui.screens.notifications.NotificationViewModel
import com.example.soymusicreviewapp.ui.screens.profile.ProfileScreen
import com.example.soymusicreviewapp.ui.screens.register.RegisterViewModel
import com.example.soymusicreviewapp.ui.screens.reviewdetail.ReviewDetailViewModel
import com.example.soymusicreviewapp.ui.screens.settings.SettingsScreen
import com.example.soymusicreviewapp.ui.screens.songdetail.SongsDetailViewModel
import com.example.soymusicreviewapp.ui.screens.start.StartViewModel
import com.example.soymusicreviewapp.ui.screens.profile.ProfileViewModel
import com.example.soymusicreviewapp.ui.screens.settings.SettingsViewModel


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
            val startViewModel: StartViewModel = hiltViewModel()
            StartScreen(
                viewModel = startViewModel,
                loginButtonPressed = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                registerButtonPressed = {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            )
        }

        composable(route = Screen.LoginScreen.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                loginButtonPressed = {
                    navController.navigate(Screen.ForYouFeedScreen.route)
                }
            )
        }

        composable(route = Screen.RegisterScreen.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = registerViewModel,
                loginCreateAccount = {
                    navController.navigate(Screen.ForYouFeedScreen.route)
                }
            )
        }

        composable(route = Screen.ForYouFeedScreen.route) {
            val forYouFeedViewModel: ForYouFeedViewModel = hiltViewModel()
            ForYouFeedScreen(
                viewModel = forYouFeedViewModel,
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
            val reviewDetailViewModel: ReviewDetailViewModel = hiltViewModel()
            ReviewDetailScreen(
                viewModel = reviewDetailViewModel,
                reviewId = reviewId,
                modifier = Modifier.padding(12.dp),
            )
        }

        composable(route = Screen.FollowingFeedScreen.route) {
            val followingFeedViewModel: FollowingFeedViewModel = hiltViewModel()
            FollowingFeedScreen(
                viewModel = followingFeedViewModel,
                onReviewClick = { reviewId ->
                    navController.navigate("reviewDetail/$reviewId")
                },
                latestButtonPressed = {
                    navController.navigate(Screen.LatestFeedScreen.route)
                }
            )
        }

        composable(route = Screen.LatestFeedScreen.route) {
            val latestFeedViewModel: LatestFeedViewModel = hiltViewModel()
            LatestFeedScreen(
                viewModel = latestFeedViewModel,
                modifier = Modifier,
                latestCreateAccount = {
                    navController.navigate(Screen.ForYouFeedScreen.route)
                }
            )
        }

        composable(route = Screen.ExploreScreen.route) {
            val exploreViewModel: ExploreViewModel = hiltViewModel()
            ExploreScreen(
                viewModel = exploreViewModel,
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
            val songsDetailViewModel: SongsDetailViewModel = hiltViewModel()
            SongsDetailScreen(
                viewModel = songsDetailViewModel,
                songId = songId,
                onBack = { navController.popBackStack() },
                onReviewClick = { reviewId ->
                    navController.navigate("reviewDetail/$reviewId")
                }
            )
        }

        composable(route = Screen.CreateReviewScreen.route) {
            val createReviewViewModel: CreateReviewViewModel = hiltViewModel()
            CreateReviewScreen(
                viewModel = createReviewViewModel,
                onSongClick = { songId ->
                    navController.navigate("songDetail/$songId")
                }
            )
        }

        composable(route = Screen.NotificationScreen.route) {
            val notificationViewModel: NotificationViewModel = hiltViewModel()
            NotificationScreen(
                viewModel = notificationViewModel
            )
        }

        composable(route = Screen.ProfileScreen.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = profileViewModel,
                settingsButtonPressed = {
                    navController.navigate(Screen.SettingsScreen.route)
                }
            )
        }

        composable(route = Screen.SettingsScreen.route) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                onConfirmLogout = {
                    navController.navigate(Screen.StartScreen.route)
                },
                onBackClick = {
                    navController.navigate(Screen.ProfileScreen.route)
                }
            )
        }


    }
}
