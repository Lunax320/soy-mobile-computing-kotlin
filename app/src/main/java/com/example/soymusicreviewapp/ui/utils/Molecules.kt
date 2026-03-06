package com.example.soymusicreviewapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
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
fun ReviewInfo(
    review: Review,
    isProfileView: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = review.userImageId),
                    contentDescription = "User image",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    UserText(user = review.userName)
                    Spacer(modifier = Modifier.height(5.dp))
                    DateText(date = review.date)
                }
            }

            if (isProfileView) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit review",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Delete review",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SongText(songName = review.songName)
            ArtistText(artistName = review.userName)
            Spacer(modifier = Modifier.height(10.dp))
            RatingText(rating = review.rating)
            Spacer(modifier = Modifier.height(10.dp))
            ReviewText(review = review.reviewText)
            Spacer(modifier = Modifier.height(7.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = "Like",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "42", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_comment),
                contentDescription = "Comment",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Comment", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
        }
    }
}

@Composable
fun SongInfo(
    song: Song,
    modifier: Modifier = Modifier,
    isNewRelease: Boolean = false,
    imageSize: Dp = 70.dp,
    titleSize: TextUnit = 16.sp,
    artistSize: TextUnit = 14.sp,
    tagSize: TextUnit = 12.sp,
    timeSize: TextUnit = 12.sp
){
    Row(
        modifier = Modifier.padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = song.imageId),
            contentDescription = "Song cover",
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column {
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
                    GenreTag("New", backgroundColor = MaterialTheme.colorScheme.surface, borderColor = MaterialTheme.colorScheme.onSurface, tagSize= tagSize)
                } else {
                    GenreTag(song.genre, tagSize= tagSize)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = song.duration,
                    color = MaterialTheme.colorScheme.surfaceBright,
                    fontSize = timeSize
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
    //It is neccesary a container (Column) to place the elements one under the other
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Option to log out
        SettingsOption(
            title = stringResource(R.string.sign_out),
            subtitle = stringResource(R.string.log_out_of_your_account),
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            // Empty function to test the visual
            onClick = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Option to delete account
        SettingsOption(
            title = stringResource(R.string.delete_account),
            subtitle = stringResource(R.string.delete_your_account_permantly),
            icon = Icons.Filled.Delete,
            containerColor = MaterialTheme.colorScheme.error,
            onClick = { }
        )
    }
}