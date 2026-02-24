package com.example.soymusicreviewapp.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.data.Review
import com.example.soymusicreviewapp.ui.data.Song
import com.example.soymusicreviewapp.ui.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.ui.data.local.LocalSongsProvider
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
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 27.dp, vertical = 15.dp)
            )
        }

        items(3) { index ->
            SongCard(
                song = songs[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                isNewRelease = true
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                text = stringResource(R.string.recent_reviews),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 27.dp, vertical = 15.dp)
            )
        }

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

@Composable
fun NewsFeedScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FeedScreenHeader(2)

        Box {
            val allReviews = LocalReviewProvider.reviews
            val allSongs = LocalSongsProvider.songs

            PlainBackground()

            LatestFeedList(
                songs = allSongs.take(3),
                reviews = allReviews,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsFeedPreview() {
    NewsFeedScreen()
}