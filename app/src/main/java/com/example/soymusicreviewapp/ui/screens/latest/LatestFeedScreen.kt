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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.FeedScreenHeader
import com.example.soymusicreviewapp.ui.utils.ReviewCard
import com.example.soymusicreviewapp.ui.utils.SongCard

@Composable
fun LatestFeedList(
    songs: List<Song>,
    reviews: List<Review>,
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

        // Display the top 3 songs
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

        // Display all reviews
        items(reviews.size) { index ->
            ReviewCard(
                review = reviews[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                isProfileView = false
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// Main screen composable
@Composable
fun LatestFeedScreen(
    modifier: Modifier = Modifier,
    latestCreateAccount: () -> Unit,
    viewModel: LatestFeedViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header navigation logic
        FeedScreenHeader(
            currentTab = 2,
            HeaderButtonPressed = latestCreateAccount
        )

        Box {
            PlainBackground()

            // Pass data from state to the list
            LatestFeedList(
                songs = state.newReleases,
                reviews = state.recentReviews,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun LatestFeedPreview() {
    CompMovilProyectoTheme {
        LatestFeedScreen(
            modifier = Modifier,
            latestCreateAccount = {}
        )
    }
}