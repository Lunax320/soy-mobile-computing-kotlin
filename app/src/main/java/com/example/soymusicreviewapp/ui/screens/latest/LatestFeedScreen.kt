package com.example.soymusicreviewapp.ui.screens.latest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.FeedScreenHeader
import com.example.soymusicreviewapp.ui.utils.ReviewCard
import com.example.soymusicreviewapp.ui.utils.SongCard

@Composable
fun LatestFeedScreen(
    modifier: Modifier = Modifier,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    latestCreateAccount: () -> Unit,
    viewModel: LatestFeedViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FeedScreenHeader(
            currentTab = 2,
            HeaderButtonPressed = latestCreateAccount
        )

        Box {
            PlainBackground()

            LatestFeedList(
                songs = state.newReleases,
                reviews = state.recentReviews,
                currentUserId = state.currentUserId,
                onReviewClick = onReviewClick,
                onUserClick = onUserClick,
                onLikeClick = { reviewId -> viewModel.sendOrDeleteReviewLike(reviewId, state.currentUserId) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun LatestFeedList(
    songs: List<Song>,
    reviews: List<Review>,
    currentUserId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.new_releases),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 27.dp, vertical = 15.dp)
            )
        }

        items(songs.size) { index ->
            SongCard(
                song = songs[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {},
                isNewRelease = true
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                text = stringResource(R.string.recent_reviews),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 27.dp, vertical = 15.dp)
            )
        }

        items(reviews.size) { index ->
            ReviewCard(
                review = reviews[index],
                currentUserId = currentUserId,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onReviewClick = onReviewClick,
                onUserClick = onUserClick,
                onLikeClick = onLikeClick,
                isProfileView = false
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LatestFeedPreview() {
    CompMovilProyectoTheme {
        LatestFeedList(
            songs = emptyList(),
            reviews = emptyList(),
            currentUserId = "1",
            onReviewClick = {},
            onUserClick = {},
            onLikeClick = {}
        )
    }
}
