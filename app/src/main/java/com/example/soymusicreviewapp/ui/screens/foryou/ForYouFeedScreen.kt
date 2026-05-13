package com.example.soymusicreviewapp.ui.screens.foryou

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
fun ForYouFeedScreen(
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    followingButtonPressed: () -> Unit,
    onMapClick: () -> Unit,
    viewModel: ForYouFeedViewModel
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
                    currentTab = 0,
                    HeaderButtonPressed = followingButtonPressed,
                    modifier = Modifier.padding(top = 8.dp)
                )

                ForYouScreenBodyContent(
                    reviews = state.reviews,
                    currentUserId = state.currentUserId,
                    onReviewClick = onReviewClick,
                    onUserClick = onUserClick,
                    onLikeClick = { reviewId ->
                        viewModel.sendOrDeleteReviewLike(reviewId, state.currentUserId)
                    },
                    onCommentClick = onCommentClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = paddingValues.calculateBottomPadding()/200)
                )
            }
        }
    }
}

@Composable
fun ForYouScreenBodyContent(
    reviews: List<Review>,
    currentUserId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ReviewList(
            onReviewClick = { reviewId -> onReviewClick(reviewId) },
            onUserClick = { userId -> onUserClick(userId) },
            onLikeClick = onLikeClick,
            onCommentClick = onCommentClick,
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.recommended_reviews_for_you),
            reviews = reviews,
            currentUserId = currentUserId
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForYouFeedScreenPreview() {
    CompMovilProyectoTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PlainBackground()
            ForYouScreenBodyContent(
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