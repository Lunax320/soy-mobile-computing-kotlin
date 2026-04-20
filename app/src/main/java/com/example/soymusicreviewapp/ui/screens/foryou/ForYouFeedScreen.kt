package com.example.soymusicreviewapp.ui.screens.foryou

import android.util.Log
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
fun ForYouFeedScreen(
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    followingButtonPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForYouFeedViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        FeedScreenHeader(
            currentTab = 0,
            HeaderButtonPressed = followingButtonPressed)

        ForYouScreenBody(
            reviews = state.reviews,
            currentUserId = state.currentUserId,
            onReviewClick = onReviewClick,
            onUserClick = onUserClick,
            onLikeClick = { reviewId -> 
                viewModel.sendOrDeleteReviewLike(reviewId, state.currentUserId)
                Log.d("ForYouFeedScreen", "el usuario ${state.currentUserId} dio like a la reseña $reviewId")
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}

@Composable
fun ForYouScreenBody(
    reviews: List<Review>,
    currentUserId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        PlainBackground()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ReviewList(
                onReviewClick = { reviewId -> onReviewClick(reviewId) },
                onUserClick = { userId -> onUserClick(userId) },
                onLikeClick = onLikeClick,
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.recommended_reviews_for_you),
                reviews = reviews,
                currentUserId = currentUserId
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForYouFeedScreenPreview() {
    CompMovilProyectoTheme {
        ForYouScreenBody(
            reviews = LocalReviewProvider.reviews,
            currentUserId = "1",
            onReviewClick = {},
            onUserClick = {},
            onLikeClick = {}
        )
    }
}
