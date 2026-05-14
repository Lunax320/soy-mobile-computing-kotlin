package com.example.soymusicreviewapp.ui.utils

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.Song

@Composable
fun ShareReviewAction(
    albumName: String,
    artistName: String,
    rating: Int,
    comment: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shareMessage = "¡Mira mi reseña en SoyMusicReviewApp! " +
            "🎧 Álbum: $albumName de $artistName. " +
            "Mi calificación: $rating/5 estrellas. " +
            "Opinión: $comment" +
            "<3"

    IconButton(
        onClick = {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share review",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ReviewInfo(
    review: Review,
    currentUserId: String,
    isProfileView: Boolean = false,
    modifier: Modifier = Modifier,
    onUserClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null,
    onLikeClick: (String) -> Unit = {},
    onCommentClick: (String) -> Unit = {}

) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = if (onUserClick != null) Modifier.clickable { onUserClick() } else Modifier
            ) {

                ReviewAsyncImage(
                    profileImage = review.profileImage,
                    size = 40
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    UserText(user = review.userName)
                    Spacer(modifier = Modifier.height(5.dp))
                    DateText(date = review.date)
                }
            }

            if (isProfileView) {
                if (review.userId == currentUserId) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit review",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    if (onEditClick != null) {
                                        onEditClick()
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        Image(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Delete review",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    if (onDeleteClick != null) {
                                        onDeleteClick()
                                    }
                                }
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SongText(songName = review.songName)
            ArtistText(artistName = review.artistName)
            Spacer(modifier = Modifier.height(10.dp))

            if (review.rating > 0) {
                RatingText(rating = review.rating)
                Spacer(modifier = Modifier.height(10.dp))
            }

            ReviewText(review = review.reviewText)
            Spacer(modifier = Modifier.height(7.dp))
        }
    }
}

@Composable
fun ReviewInteractionBar(
    likesCount: Int,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    albumName: String,
    artistName: String,
    rating: Int,
    comment: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(6.dp))
        IconButton(
            onClick = onLikeClick,
            modifier = Modifier.testTag("likeButton")

        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "",
                tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = likesCount.toString(),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("likesCount")
        )
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = onCommentClick) {
            Icon(
                imageVector = Icons.Outlined.Comment,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ShareReviewAction(
            albumName = albumName,
            artistName = artistName,
            rating = rating,
            comment = comment
        )
    }
}

@Composable
fun SongInfo(
    song: Song,
    modifier: Modifier = Modifier,
    isNewRelease: Boolean = false,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
    imageSize: Dp = 70.dp,
    titleSize: TextUnit = 16.sp,
    artistSize: TextUnit = 14.sp,
    tagSize: TextUnit = 12.sp,
    timeSize: TextUnit = 12.sp
) {
    Row(
        modifier = modifier.padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SongAsyncImage(
            profileImage = song.songImage,
            size = imageSize
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column(modifier = Modifier.weight(1f)) {
            SongText(
                songName = song.name,
                fontSize = titleSize
            )

            Spacer(modifier = Modifier.height(5.dp))

            ArtistText(
                artistName = song.artist,
                fontSize = artistSize
            )

            Spacer(modifier = Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                if (isNewRelease) {
                    GenreTag("New", backgroundColor = MaterialTheme.colorScheme.surface, borderColor = MaterialTheme.colorScheme.onSurface, tagSize = tagSize)
                } else {
                    GenreTag(song.genre, tagSize = tagSize)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = song.duration,
                    color = MaterialTheme.colorScheme.surfaceBright,
                    fontSize = timeSize
                )
            }
        }

        // Botón de favorito (estrella)
        if (onFavoriteClick != null) {
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.testTag("favoriteButton_${song.songId}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun SettingsOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun SettingOptionsPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        SettingsOption(
            title = stringResource(R.string.sign_out),
            subtitle = stringResource(R.string.log_out_of_your_account),
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            onClick = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsOption(
            title = stringResource(R.string.delete_account),
            subtitle = stringResource(R.string.delete_your_account_permantly),
            icon = Icons.Filled.Delete,
            containerColor = MaterialTheme.colorScheme.error,
            onClick = { }
        )
    }
}

@Composable
@Preview
fun PreviewReviewInteractionBar(
    modifier: Modifier = Modifier
) {
    ReviewInteractionBar(
        likesCount = 10,
        isLiked = true,
        onLikeClick = {},
        onCommentClick = {},
        albumName = "Album",
        artistName = "Artist",
        rating = 5,
        comment = "Comment"
    )
}
