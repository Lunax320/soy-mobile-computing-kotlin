package com.example.soymusicreviewapp.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewInfo


@Composable
fun ReviewDetailScreen(
    reviewInfo: Review,
    responseReviews: List<Review>,
    modifier: Modifier = Modifier
) {

    Box(modifier = Modifier.fillMaxSize()) {

        PlainBackground()

        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                ReviewInfo(
                    review = reviewInfo,
                    isProfileView = true
                )

                HorizontalDivider(thickness = 1.dp, color = Color.Gray)

                ReviewActionBar(
                    onLike = { /*  */ },
                    onComment = { /*  */ },
                    onShare = { /*  */ },
                    onFavorite = { /*  */ },
                    isLiked = false
                )

                HorizontalDivider(thickness = 1.dp, color = Color.Gray)

                Text(
                    text = "Most relevant reviews",
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp)
                )
            }

            items(responseReviews.size) { index ->
                ReviewInfo(
                    review = responseReviews[index],
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color.DarkGray)
            }
        }
    }
}


@Composable
fun ReviewActionBar(
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
    isLiked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(onClick = onLike) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) Color.Red else Color.White
            )
        }

        IconButton(onClick = onComment) {
            Icon(
                imageVector = Icons.Outlined.Send,
                contentDescription = "Comment",
                tint = Color.White
            )
        }

        IconButton(onClick = onFavorite) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Comment",
                tint = Color.White
            )
        }

        IconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Share",
                tint = Color.White
            )
        }
    }
}

@Composable
@Preview
fun ReviewActionBarPreview() {
    ReviewActionBar(
        onLike = {},
        onComment = {},
        onShare = {},
        onFavorite = {},
        isLiked = false
    )
}