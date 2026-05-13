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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.soymusicreviewapp.ui.utils.MapFloatingActionButton
import com.example.soymusicreviewapp.ui.utils.ReviewCard
import com.example.soymusicreviewapp.ui.utils.SongCard

@Composable
fun LatestFeedScreen(
    modifier: Modifier = Modifier,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onMapClick: () -> Unit,
    latestCreateAccount: () -> Unit,
    viewModel: LatestFeedViewModel
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
                    currentTab = 2,
                    HeaderButtonPressed = latestCreateAccount,
                    modifier = Modifier.padding(top = 8.dp)
                )

                LatestFeedList(
                    songs = state.newReleases,
                    reviews = state.recentReviews,
                    currentUserId = state.currentUserId,
                    onReviewClick = onReviewClick,
                    onUserClick = onUserClick,
                    onLikeClick = { reviewId -> viewModel.sendOrDeleteReviewLike(reviewId, state.currentUserId) },
                    onCommentClick = onCommentClick,
                    favoriteSongsIds = state.favoriteSongsIds,
                    onFavoriteClick = { songId -> viewModel.onFavoriteClick(songId) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = paddingValues.calculateBottomPadding() / 200)
                )
            }
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
    onCommentClick: (String) -> Unit,
    favoriteSongsIds: Set<String> = emptySet(),
    onFavoriteClick: ((String) -> Unit)? = null,
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
                onClick = {},
                isNewRelease = true,
                isFavorite = favoriteSongsIds.contains(songs[index].songId),
                onFavoriteClick = if (onFavoriteClick != null) {
                    { onFavoriteClick(songs[index].songId) }
                } else null
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
                onCommentClick = onCommentClick,
                isProfileView = false
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}