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
fun ForYouScreenBody(
    modifier: Modifier = Modifier
) {
    val allReviews = LocalReviewProvider.reviews
    Box(modifier = modifier) {
        PlainBackground()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ReviewList(allReviews, modifier = Modifier.weight(1f), title = stringResource(R.string.recommended_reviews_for_you))
        }
    }
}

@Composable
fun ForYouScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FeedScreenHeader(0)
        ForYouScreenBody(
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
fun ForYouScreenPreview() {
    ForYouScreen()
}