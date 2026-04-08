package com.example.soymusicreviewapp.ui.screens.foryou

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

// Main screen composable connecting ViewModel and UI
@Composable
fun ForYouFeedScreen(
    onReviewClick: (String) -> Unit,
    followingButtonPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForYouFeedViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header component with navigation callback
        FeedScreenHeader(
            currentTab = 0,
            HeaderButtonPressed = followingButtonPressed)

        // Body receives data from state
        ForYouScreenBody(
            reviews = state.reviews,
            onReviewClick = onReviewClick,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}

// Composable for the screen body, receiving the reviews list as parameter
@Composable
fun ForYouScreenBody(
    reviews: List<Review>,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        PlainBackground()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Displays the list of reviews using data from state
            ReviewList(
                onReviewClick = { reviewId -> onReviewClick(reviewId) },
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.recommended_reviews_for_you),
                reviews = reviews
            )
        }
    }
}


//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun ForYouFeedScreenPreview() {
    CompMovilProyectoTheme {
        ForYouFeedScreen(
            viewModel = viewModel(),
            onReviewClick = {},
            followingButtonPressed = {}
        )
    }
}