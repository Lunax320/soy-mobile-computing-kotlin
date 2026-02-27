package com.example.soymusicreviewapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.Song
import kotlin.Boolean

@Composable
fun FeedScreenHeader(
    currentTab: Int,
    modifier: Modifier = Modifier
) {
    val colorForYou: Int
    if (currentTab == 0) {
        colorForYou = R.color.azulcal
    } else {
        colorForYou = R.color.violetaClaro
    }

    val colorFollowing: Int
    if (currentTab == 1) {
        colorFollowing = R.color.azulcal
    } else {
        colorFollowing = R.color.violetaClaro
    }

    val colorLatest: Int
    if (currentTab == 2) {
        colorLatest = R.color.azulcal
    } else {
        colorLatest = R.color.violetaClaro
    }

    Box() {
        Image(
            painter = painterResource(R.drawable.bg_plain_top_v3),
            contentDescription = stringResource(R.string.top_background))

        Column(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Row() {
                Spacer(modifier = Modifier.width(9.dp))
                LogoSoy(modifier = Modifier.size(57.dp))
                Spacer(modifier = Modifier.width(12.dp))
                TextSoy(size = 45.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                GeneralButton(
                    text = "For you",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorForYou
                )
                GeneralButton(
                    text = "Social",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorFollowing
                )
                GeneralButton(
                    text = "Latest",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorLatest
                )
            }
        }
    }
}

@Composable
fun ReviewCard(
    review: Review,
    onReviewClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    isProfileView: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.vclaro)
        ),
        onClick = { onReviewClick(review.usernameId) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ReviewInfo(
            review = review,
            isProfileView = isProfileView
        )
    }
}

@Composable
fun ReviewList(
    onReviewClick: (Int) -> Unit = {},
    reviews: List<Review>,
    title: String,
    modifier: Modifier = Modifier,
    isProfileView: Boolean = false

) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }

        items(reviews.size) { index ->
            ReviewCard(
                review = reviews[index],
                modifier = Modifier.fillMaxWidth(),
                isProfileView = isProfileView,
                onReviewClick = onReviewClick
            )
        }
    }
}

@Composable
fun SongCard(
    song: Song,
    modifier: Modifier = Modifier,
    isNewRelease: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.vclaro)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = song.imageId),
                contentDescription = "Song cover",
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(15.dp))

            Column {
                SongText(songName = song.name)

                Spacer(modifier = Modifier.height(5.dp))

                ArtistText(artistName = song.artist)

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    if (isNewRelease) {
                        GenreTag("New", backgroundColor = R.color.amarilloOscuro, borderColor = R.color.amarilloClaro)
                    } else {
                        GenreTag(song.genre)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = song.duration,
                        color = colorResource(R.color.vclaroletra),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SongList(
    songs: List<Song>,
    modifier: Modifier = Modifier,
    isNewRelease: Boolean = false
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(songs.size) { index ->
            SongCard(
                song = songs[index],
                modifier = Modifier.fillMaxWidth(),
                isNewRelease = isNewRelease
            )
        }
    }
}