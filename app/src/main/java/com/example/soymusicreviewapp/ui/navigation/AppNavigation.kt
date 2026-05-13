package com.example.soymusicreviewapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.soymusicreviewapp.ui.screens.songdetail.SongsDetailScreen
import com.example.soymusicreviewapp.ui.screens.foryou.ForYouFeedScreen
import com.example.soymusicreviewapp.ui.screens.following.FollowingFeedScreen
import com.example.soymusicreviewapp.ui.screens.latest.LatestFeedScreen
import com.example.soymusicreviewapp.ui.screens.favorites.FavoritesScreen
import com.example.soymusicreviewapp.ui.screens.profile.ProfileScreen
import com.example.soymusicreviewapp.ui.screens.profile.ProfileViewModel
import com.example.soymusicreviewapp.ui.screens.settings.SettingsScreen
import com.example.soymusicreviewapp.ui.screens.editreview.EditReviewScreen
import com.example.soymusicreviewapp.ui.screens.createreview.CreateReviewViewModel
import com.example.soymusicreviewapp.ui.screens.reviewdetail.ReviewDetailScreen
import com.example.soymusicreviewapp.ui.screens.editprofile.EditProfileScreen
import com.example.soymusicreviewapp.ui.screens.commentreview.CommentReviewScreen
import com.example.soymusicreviewapp.ui.screens.followersDetail.FollowersDetailScreen
import com.example.soymusicreviewapp.ui.screens.followingdetail.FollowingDetailScreen
import com.example.soymusicreviewapp.ui.screens.createreview.CreateReviewScreen as ActualCreateReviewScreen
import com.example.soymusicreviewapp.ui.screens.reviewsmap.ReviewsMapScreen as ActualReviewsMapScreen

sealed class Screen(val route: String) {
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
    object ReviewDetailScreen : Screen("reviewDetail")
    object EditProfileScreen : Screen("editProfile")
    object FollowersDetailScreen : Screen("followersDetail")
    object FollowingDetailScreen : Screen("followingDetail")
    object ReviewsMapScreen : Screen("reviewsMap")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(
                navigateToHome = {
                    navController.navigate(Screen.ForYouFeedScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                },
                navigateToStart = {
                    navController.navigate(Screen.StartScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                },
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
            LoginScreen(
                viewModel = hiltViewModel(),
                navigateToHome = { navController.navigate(Screen.ForYouFeedScreen.route) }
            )
        }

        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(
                viewModel = hiltViewModel(),
                navigateToHome = { navController.navigate(Screen.ForYouFeedScreen.route) }
            )
        }

        composable(route = Screen.ForYouFeedScreen.route) {
            ForYouFeedScreen(
                viewModel = hiltViewModel(),
                onReviewClick = { reviewId -> navController.navigate("reviewDetail/$reviewId") },
                onUserClick = { userId -> navController.navigate("userProfile/$userId") },
                onCommentClick = { reviewId -> navController.navigate("commentReview/$reviewId") },
                followingButtonPressed = { navController.navigate(Screen.FollowingFeedScreen.route) },
                onMapClick = { navController.navigate(Screen.ReviewsMapScreen.route) }
            )
        }

        composable(route = Screen.FollowingFeedScreen.route) {
            FollowingFeedScreen(
                viewModel = hiltViewModel(),
                onReviewClick = { reviewId -> navController.navigate("reviewDetail/$reviewId") },
                onUserClick = { userId -> navController.navigate("userProfile/$userId") },
                onCommentClick = { reviewId -> navController.navigate("commentReview/$reviewId") },
                latestButtonPressed = { navController.navigate(Screen.LatestFeedScreen.route) },
                onMapClick = { navController.navigate(Screen.ReviewsMapScreen.route) }
            )
        }

        composable(route = Screen.LatestFeedScreen.route) {
            LatestFeedScreen(
                viewModel = hiltViewModel(),
                onReviewClick = { reviewId -> navController.navigate("reviewDetail/$reviewId") },
                onUserClick = { userId -> navController.navigate("userProfile/$userId") },
                onCommentClick = { reviewId -> navController.navigate("commentReview/$reviewId") },
                latestCreateAccount = { navController.navigate(Screen.ForYouFeedScreen.route) },
                onMapClick = { navController.navigate(Screen.ReviewsMapScreen.route) }
            )
        }

        composable(
            route = "reviewDetail/{reviewId}",
            arguments = listOf(navArgument("reviewId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reviewId = backStackEntry.arguments?.getString("reviewId") ?: ""
            ReviewDetailScreen(
                reviewId = reviewId,
                viewModel = hiltViewModel(),
                onReviewClick = { rId -> navController.navigate("reviewDetail/$rId") },
                onCommentClick = { reviewId -> navController.navigate("commentReview/$reviewId") },
                onUserClick = { userId -> navController.navigate("userProfile/$userId") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen(
                viewModel = hiltViewModel(),
                onSongClick = { songId -> navController.navigate("songDetail/$songId") }
            )
        }

        composable(
            route = "createReview/{songId}",
            arguments = listOf(navArgument("songId") { type = NavType.StringType })
        ) { 
            // Corregido: Ya no pasamos parámetros manuales.
            // ActualCreateReviewScreen obtiene todo lo necesario internamente.
            ActualCreateReviewScreen(
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
                onReviewClick = { reviewId -> navController.navigate("reviewDetail/$reviewId") },
                onUserClick = { userId -> navController.navigate("userProfile/$userId") },
                onCommentClick = { reviewId -> navController.navigate("commentReview/$reviewId") }
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
            val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

            if (currentUserId.isNotEmpty()) {
                ProfileScreen(
                    userId = currentUserId,
                    viewModel = profileViewModel,
                    settingsButtonPressed = { navController.navigate(Screen.SettingsScreen.route) },
                    onEditReview = { rId, sId ->
                        navController.navigate("editReview/$rId/$sId")
                    },
                    onEditProfileClick = { navController.navigate(Screen.EditProfileScreen.route) },
                    onReviewClick = { reviewId -> navController.navigate("reviewDetail/$reviewId") },
                    onUserClick = { userId -> navController.navigate("userProfile/$userId") },
                    onCommentClick = { reviewId -> navController.navigate("commentReview/$reviewId") },
                    onFollowersClick = { userId -> navController.navigate("followersDetail/$userId") },
                    onFollowingClick = { userId -> navController.navigate("followingDetail/$userId") },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = "userProfile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val profileViewModel: ProfileViewModel = hiltViewModel()

            if (userId.isNotEmpty()) {
                ProfileScreen(
                    userId = userId,
                    viewModel = profileViewModel,
                    settingsButtonPressed = { },
                    onEditReview = { _, _ -> },
                    onReviewClick = { reviewId -> navController.navigate("reviewDetail/$reviewId") },
                    onUserClick = { clickedUserId -> navController.navigate("userProfile/$clickedUserId") },
                    onCommentClick = { reviewId -> navController.navigate("commentReview/$reviewId") },
                    onFollowersClick = { uId -> navController.navigate("followersDetail/$uId") },
                    onFollowingClick = { uId -> navController.navigate("followingDetail/$uId") },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(route = Screen.NotificationScreen.route) {
            FavoritesScreen(
                viewModel = hiltViewModel(),
                onSongClick = { songId ->
                    navController.navigate("songDetail/$songId")
                }
            )
        }

        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(
                viewModel = hiltViewModel(),
                onConfirmLogout = {
                    navController.navigate(Screen.StartScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.EditProfileScreen.route) {
            EditProfileScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = "followersDetail/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            FollowersDetailScreen(
                viewModel = hiltViewModel(),
                userId = userId,
                onBackClick = { navController.popBackStack() },
                onUserClick = { clickedUserId -> navController.navigate("userProfile/$clickedUserId") }
            )
        }

        composable(
            route = "commentReview/{reviewId}",
            arguments = listOf(navArgument("reviewId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reviewId = backStackEntry.arguments?.getString("reviewId") ?: ""
            CommentReviewScreen(
                reviewId = reviewId,
                onCloseClick = { navController.popBackStack() },
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = "followingDetail/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            FollowingDetailScreen(
                viewModel = hiltViewModel(),
                userId = userId,
                onBackClick = { navController.popBackStack() },
                onUserClick = { clickedUserId -> navController.navigate("userProfile/$clickedUserId") }
            )
        }

        composable(route = Screen.ReviewsMapScreen.route) {
            ActualReviewsMapScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = hiltViewModel()
            )
        }
    }
}
