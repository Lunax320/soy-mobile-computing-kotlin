package com.example.soymusicreviewapp.ui.screens.reviewdetail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.soymusicreviewapp.ui.utils.BackButton
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewInfo
import com.example.soymusicreviewapp.ui.utils.ShareReviewAction

@Composable
fun ReviewDetailScreen(
    reviewId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ReviewDetailViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(reviewId) {
        viewModel.loadReview(reviewId)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .testTag("reviewDetailScreen")
    ) {
        PlainBackground()

        if (state.selectedReview != null) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                item {

                    Spacer(modifier = Modifier.padding(top = 45.dp))

                    ReviewInfo(
                        review = state.selectedReview!!,
                        currentUserId = state.currentUserId,
                        isProfileView = true,
                        onDeleteClick = {},
                        onEditClick = {},
                        onUserClick = { onUserClick(state.selectedReview!!.userId) }
                    )

                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                    ReviewActionBar(
                        onLike = {
                            Log.d("ReviewDetailScreen", "Usuario ${state.currentUserId} dio like a $reviewId")
                        },
                        onFavorite = { /* */ },
                        onComment = { onCommentClick(reviewId) },
                        isLiked = state.selectedReview?.liked ?: false,
                        albumName = state.selectedReview!!.songName,
                        artistName = state.selectedReview!!.artistName,
                        rating = state.selectedReview!!.rating,
                        comment = state.selectedReview!!.reviewText,
                        modifier = Modifier.testTag("reviewActionBar")
                    )

                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                    Text(
                        text = "Comments",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp)
                    )
                }

                items(state.responseReviews.size) { index ->
                    val responseReview = state.responseReviews[index]
                    ReviewInfo(
                        review = responseReview,
                        currentUserId = state.currentUserId,
                        modifier = Modifier.padding(vertical = 4.dp),
                        onDeleteClick = {},
                        onEditClick = {},
                        onUserClick = { onUserClick(responseReview.userId) }
                    )
                    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onTertiary)
                }
            }
        }

        BackButton(
            onBack = onBackClick,
            modifier = Modifier
                .padding(start = 6.dp, top = 8.dp)
                .testTag("backButton")
        )
    }
}

@Composable
fun ReviewActionBar(
    modifier: Modifier = Modifier,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onFavorite: () -> Unit,
    isLiked: Boolean,
    albumName: String,
    artistName: String,
    rating: Int,
    comment: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(
            onClick = onLike,
            modifier = Modifier.testTag("detailLikeButton")) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimary
            )
        }

        IconButton(onClick = onComment) {
            Icon(
                imageVector = Icons.Outlined.Send,
                contentDescription = "Comment",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        IconButton(onClick = onFavorite) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Favorite",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        ShareReviewAction(
            albumName = albumName,
            artistName = artistName,
            rating = rating,
            comment = comment
        )
    }
}
