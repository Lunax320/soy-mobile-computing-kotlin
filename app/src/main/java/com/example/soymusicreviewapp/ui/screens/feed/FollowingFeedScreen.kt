package com.example.soymusicreviewapp.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.FeedScreenHeader
import com.example.soymusicreviewapp.ui.utils.ReviewList

@Composable
fun FollowingFeedScreenBody(
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val allReviews = LocalReviewProvider.reviews
    Box(modifier = modifier) {
        PlainBackground()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ReviewList(
                onReviewClick = { reviewId -> onReviewClick(reviewId) },
                allReviews, modifier = Modifier.weight(1f),
                title = stringResource(R.string.reviews_from_users_you_follow))
        }
    }
}

@Composable
fun FollowingFeedScreen(
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FeedScreenHeader(1)
        FollowingFeedScreenBody(
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
    FollowingFeedScreen(
        onReviewClick = {}
    )
}