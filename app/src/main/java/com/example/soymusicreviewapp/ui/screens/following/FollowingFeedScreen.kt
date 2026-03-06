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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.FeedScreenHeader
import com.example.soymusicreviewapp.ui.utils.ReviewList

// Composable for the screen body, receiving the list of reviews as a parameter
@Composable
fun FollowingFeedScreenBody(
    reviews: List<Review>,
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        PlainBackground()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Displays the list of reviews passed from the state
            ReviewList(
                onReviewClick = { reviewId -> onReviewClick(reviewId) },
                reviews = reviews,
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.reviews_from_users_you_follow)
            )
        }
    }
}

// Main screen composable that integrates ViewModel and Navigation
@Composable
fun FollowingFeedScreen(
    latestButtonPressed: () -> Unit,
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FollowingFeedViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header with navigation logic maintained
        FeedScreenHeader(1,
            HeaderButtonPressed = latestButtonPressed)

        // Body receives the data from the state
        FollowingFeedScreenBody(
            reviews = state.reviews,
            onReviewClick = onReviewClick,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun FollowingFeedScreenPreview() {
    CompMovilProyectoTheme {
        FollowingFeedScreen(
            onReviewClick = {},
            latestButtonPressed = {}
        )
    }
}