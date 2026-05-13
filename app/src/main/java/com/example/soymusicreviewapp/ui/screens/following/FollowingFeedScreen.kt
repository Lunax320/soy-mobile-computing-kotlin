package com.example.soymusicreviewapp.ui.screens.following

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.FeedScreenHeader
import com.example.soymusicreviewapp.ui.utils.MapFloatingActionButton
import com.example.soymusicreviewapp.ui.utils.ReviewList

@Composable
fun FollowingFeedScreen(
    latestButtonPressed: () -> Unit,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FollowingFeedViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            MapFloatingActionButton(onNavigateToMap = onMapClick)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            PlainBackground()

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                FeedScreenHeader(
                    currentTab = 1,
                    HeaderButtonPressed = latestButtonPressed,
                    modifier = Modifier.padding(top = 8.dp)
                )

                FollowingFeedScreenBody(
                    reviews = state.reviews,
                    currentUserId = state.currentUserId,
                    onReviewClick = onReviewClick,
                    onUserClick = onUserClick,
                    onLikeClick = { reviewId -> viewModel.sendOrDeleteReviewLike(reviewId, state.currentUserId) },
                    onCommentClick = onCommentClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = paddingValues.calculateBottomPadding() / 200)
                )
            }
        }
    }
}

@Composable
fun FollowingFeedScreenBody(
    reviews: List<Review>,
    currentUserId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ReviewList(
        onReviewClick = onReviewClick,
        onUserClick = onUserClick,
        onLikeClick = onLikeClick,
        onCommentClick = onCommentClick,
        reviews = reviews,
        currentUserId = currentUserId,
        modifier = modifier,
        title = stringResource(R.string.reviews_from_users_you_follow)
    )
}

@Preview(showBackground = true)
@Composable
fun FollowingFeedScreenPreview() {
    CompMovilProyectoTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PlainBackground()
            FollowingFeedScreenBody(
                reviews = LocalReviewProvider.reviews,
                currentUserId = "1",
                onReviewClick = {},
                onUserClick = {},
                onLikeClick = {},
                onCommentClick = {}
            )
        }
    }
}