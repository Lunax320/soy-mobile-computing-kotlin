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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import kotlin.Boolean

@Composable
fun FeedScreenHeader(
    currentTab: Int,
    HeaderButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorForYou =
        if (currentTab == 0) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.secondary
        }

    val colorFollowing =
        if (currentTab == 1) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.secondary
        }

    val colorLatest =
        if (currentTab == 2) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.secondary
        }

    Box() {
        Image(
            painter = painterResource(R.drawable.bg_plain_top_v3),
            contentDescription = stringResource(R.string.top_background)
        )
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
                    text = "For You",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorForYou,
                    onClick = {
                        HeaderButtonPressed()
                    }
                )
                GeneralButton(
                    text = "Social",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorFollowing,
                    onClick = {
                        HeaderButtonPressed()
                    }
                )
                GeneralButton(
                    text = "Latest",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorLatest,
                    onClick = {
                        HeaderButtonPressed()
                    }
                )
            }
        }
    }
}


@Composable
fun ReviewCard(
    review: Review,
    currentUserId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit = {},
    onCommentClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    isProfileView: Boolean = false,
    onDeleteClick: ((String) -> Unit)? = null,
    onEditClick: ((String) -> Unit)? = null
) {
    Surface(
        onClick = { onReviewClick(review.id) },
        color = MaterialTheme.colorScheme.surfaceDim,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(vertical = 8.dp)
            .testTag("reviewCard_${review.id}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {

            ReviewInfo(
                review = review,
                currentUserId = currentUserId,
                isProfileView = isProfileView,
                onUserClick = { onUserClick(review.userId) },
                onDeleteClick = if (onDeleteClick != null) { { onDeleteClick(review.id) } } else null,
                onEditClick = if (onEditClick != null) { { onEditClick(review.id) } } else null,
                onLikeClick = onLikeClick,
                onCommentClick = onCommentClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReviewInteractionBar(
                likesCount = review.likesCount,
                isLiked = review.liked,
                onLikeClick = { onLikeClick(review.id) },
                onCommentClick = { onCommentClick(review.id) }
            )
        }
    }
}

@Composable
fun ReviewList(
    reviews: List<Review>,
    currentUserId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit = {},
    onCommentClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    title: String = "",
    isProfileView: Boolean = false,
    onDeleteClick: ((String) -> Unit)? = null,
    onEditClick: ((String) -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 15.dp)
    ) {
        if (title.isNotEmpty()) {

            item{
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        items(reviews.size) { index ->
            ReviewCard(
                review = reviews[index],
                currentUserId = currentUserId,
                onReviewClick = onReviewClick,
                onLikeClick = onLikeClick,
                onCommentClick = onCommentClick,
                onUserClick = onUserClick,
                isProfileView = isProfileView,
                onDeleteClick = {
                    if (onDeleteClick != null) {
                        onDeleteClick(reviews[index].id)
                    }
                },
                onEditClick = {
                    if (onEditClick != null) {
                        onEditClick(reviews[index].id)
                    }
                }
            )
        }
    }
}


@Preview
@Composable
fun ReviewCardpreview() {
    CompMovilProyectoTheme {
        ReviewCard(
            review = LocalReviewProvider.reviews[0],
            modifier = Modifier.fillMaxSize(),
            onReviewClick = {},
            onUserClick = {},
            onLikeClick = {},
            onCommentClick = {},
            currentUserId = "1"
        )
    }
}
@Preview
@Composable
fun ReviewListpreview(){
    CompMovilProyectoTheme {
        ReviewList(
            onReviewClick = {},
            onUserClick = {},
            reviews = LocalReviewProvider.reviews,
            title = stringResource(R.string.recommended_reviews_for_you),
            modifier = Modifier.fillMaxSize(),
            currentUserId = "1"
        )
    }
}
@Composable
fun SongCard(
    song: Song,
    onClick: () -> Unit,
    isNewRelease: Boolean = false,
    modifier: Modifier = Modifier

) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceDim,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        SongInfo(
            song = song,
            isNewRelease = isNewRelease
        )
    }
}

@Composable
fun SongList(
    songs: List<Song>,
    onSongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isNewRelease: Boolean = false
) {
    LazyColumn(modifier = modifier) {
        items(songs.size) { index ->
            SongCard(
                song = songs[index],
                onClick = { onSongClick(songs[index].songId) }
            )
        }
    }
}
