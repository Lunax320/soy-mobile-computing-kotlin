package com.example.soymusicreviewapp.ui.screens.following

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.FeedScreenHeader
import com.example.soymusicreviewapp.ui.utils.ReviewList

@Composable
fun FollowingFeedScreen(
    latestButtonPressed: () -> Unit,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FollowingFeedViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FeedScreenHeader(1, HeaderButtonPressed = latestButtonPressed)

        FollowingFeedScreenBody(
            reviews = state.reviews,
            currentUserId = state.currentUserId,
            onReviewClick = onReviewClick,
            onUserClick = onUserClick,
            onLikeClick = { reviewId -> viewModel.sendOrDeleteReviewLike(reviewId, state.currentUserId) },
            modifier = Modifier.fillMaxSize().weight(1f)
        )
    }
}

@Composable
fun FollowingFeedScreenBody(
    reviews: List<Review>,
    currentUserId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        PlainBackground()
        Column(modifier = Modifier.fillMaxSize()) {
            ReviewList(
                onReviewClick = onReviewClick,
                onUserClick = onUserClick,
                onLikeClick = onLikeClick,
                reviews = reviews,
                currentUserId = currentUserId,
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.reviews_from_users_you_follow)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FollowingFeedScreenPreview() {
    CompMovilProyectoTheme {
        FollowingFeedScreenBody(
            reviews = LocalReviewProvider.reviews,
            currentUserId = "1",
            onReviewClick = {},
            onUserClick = {},
            onLikeClick = {}
        )
    }
}
