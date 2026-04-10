package com.example.soymusicreviewapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.soymusicreviewapp.ui.Splash.SplashScreen
import com.example.soymusicreviewapp.ui.screens.start.StartScreen
import com.example.soymusicreviewapp.ui.screens.login.LoginScreen
import com.example.soymusicreviewapp.ui.screens.register.RegisterScreen
import com.example.soymusicreviewapp.ui.screens.explore.ExploreScreen
import com.example.soymusicreviewapp.ui.screens.searchsong.CreateReviewScreen
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
import com.example.soymusicreviewapp.ui.screens.editreview.EditReviewScreen
import com.example.soymusicreviewapp.ui.screens.createreview.CreateReviewViewModel
import com.example.soymusicreviewapp.ui.screens.createreview.CreateReviewScreen as ActualCreateReviewScreen

sealed class Screen (val route: String) {
    object SplashScreen : Screen("splash")
    object StartScreen : Screen("start")
    object LoginScreen : Screen("login")
    object RegisterScreen : Screen("register")
    object ForYouFeedScreen : Screen("forYouFeed")
    object FollowingFeedScreen : Screen("followingFeed")
    object LatestFeedScreen : Screen("latestFeed")
    object ExploreScreen : Screen("explore")
    object CreateReviewScreen : Screen("searchsong")
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
        startDestination = Screen.SplashScreen.route,
        modifier = modifier
    ) {
        composable (route = Screen.SplashScreen.route) {
            SplashScreen(
                navigateToHome = { navController.navigate(Screen.ForYouFeedScreen.route) { popUpTo(Screen.SplashScreen.route) { inclusive = true } } },
                navigateToStart = { navController.navigate(Screen.StartScreen.route) { popUpTo(Screen.SplashScreen.route) { inclusive = true } } },
                splashViewModel = hiltViewModel()
            )
        }

        composable(route = Screen.StartScreen.route) {
            StartScreen(
                viewModel = hiltViewModel(),
                loginButtonPressed = { navController.navigate(Screen.LoginScreen.route) },
                registerButtonPressed = { navController.navigate(Screen.RegisterScreen.route) }
            )
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(viewModel = hiltViewModel(), navigateToHome = { navController.navigate(Screen.ForYouFeedScreen.route) })
        }

        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(viewModel = hiltViewModel(), navigateToHome = { navController.navigate(Screen.ForYouFeedScreen.route) })
        }

        composable(route = Screen.ForYouFeedScreen.route) {
            ForYouFeedScreen(
                viewModel = hiltViewModel(),
                onReviewClick = { userId -> navController.navigate("userProfile/$userId") },
                followingButtonPressed = { navController.navigate(Screen.FollowingFeedScreen.route) }
            )
        }

        composable(route = Screen.FollowingFeedScreen.route) {
            FollowingFeedScreen(
                viewModel = hiltViewModel(),
                onReviewClick = { userId -> navController.navigate("userProfile/$userId") },
                latestButtonPressed = { navController.navigate(Screen.LatestFeedScreen.route) }
            )
        }

        composable(route = Screen.LatestFeedScreen.route) {
            LatestFeedScreen(viewModel = hiltViewModel(), latestCreateAccount = { navController.navigate(Screen.ForYouFeedScreen.route) })
        }

        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen(viewModel = hiltViewModel(), onSongClick = { songId -> navController.navigate("songDetail/$songId") })
        }

        composable(
            route = "createReview/{songId}",
            arguments = listOf(navArgument("songId") { type = NavType.StringType })
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getString("songId") ?: "1"
            val createViewModel: CreateReviewViewModel = hiltViewModel()

            ActualCreateReviewScreen(
                songId = songId,
                viewModel = createViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.CreateReviewScreen.route) {
            CreateReviewScreen(
                viewModel = hiltViewModel(),
                onSongClick = { songId -> navController.navigate("createReview/$songId") }
            )
        }

        composable(
            route = "songDetail/{songId}",
            arguments = listOf(navArgument("songId") { type = NavType.StringType })
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getString("songId") ?: "1"
            SongsDetailScreen(
                viewModel = hiltViewModel(),
                songId = songId,
                onBack = { navController.popBackStack() },
                onReviewClick = { userId -> navController.navigate("userProfile/$userId") }
            )
        }

        composable(
            route = "editReview/{reviewId}/{songId}",
            arguments = listOf(
                navArgument("reviewId") { type = NavType.StringType },
                navArgument("songId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rId = backStackEntry.arguments?.getString("reviewId") ?: ""
            val sId = backStackEntry.arguments?.getString("songId") ?: ""
            EditReviewScreen(
                reviewId = rId,
                songId = sId,
                onBackClick = { navController.popBackStack() },
                viewModel = hiltViewModel()
            )
        }

        composable(route = Screen.ProfileScreen.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            LaunchedEffect(Unit) { profileViewModel.loadUserReviews("1") }

            ProfileScreen(
                viewModel = profileViewModel,
                settingsButtonPressed = { navController.navigate(Screen.SettingsScreen.route) },
                onEditReview = { rId, sId -> navController.navigate("editReview/$rId/$sId") }
            )
        }

        composable(
            route = "userProfile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "1"
            val profileViewModel: ProfileViewModel = hiltViewModel()
            LaunchedEffect(userId) { profileViewModel.loadUserReviews(userId) }

            ProfileScreen(
                viewModel = profileViewModel,
                settingsButtonPressed = { },
                onEditReview = { _, _ -> }
            )
        }

        composable(route = Screen.NotificationScreen.route) { NotificationScreen(viewModel = hiltViewModel()) }

        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(
                viewModel = hiltViewModel(),
                onConfirmLogout = { navController.navigate(Screen.StartScreen.route) { popUpTo(0) { inclusive = true } } },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}