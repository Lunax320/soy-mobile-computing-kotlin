package com.example.soymusicreviewapp.ui.screens.reviewdetail

import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewInfo

@Composable
fun ReviewDetailScreen(
    reviewId: String,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCommentClick: (String) -> Unit,
    viewModel: ReviewDetailViewModel
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(reviewId) {
        viewModel.loadReview(reviewId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PlainBackground()

        if (state.selectedReview != null) {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                item {
                    ReviewInfo(
                        review = state.selectedReview!!,
                        currentUserId = state.currentUserId,
                        isProfileView = true,
                        onDeleteClick = {},
                        onEditClick = {},
                        onUserClick = { onUserClick(state.selectedReview!!.userId) }
                    )

                    HorizontalDivider(thickness = 1.dp, color =  MaterialTheme.colorScheme.tertiary)

                    ReviewActionBar(
                        onLike = {
                            Log.d("ReviewDetailScreen", "Usuario ${state.currentUserId} dio like a $reviewId")
                        },
                        onShare = { /* */ },
                        onFavorite = { /* */ },
                        onComment = { onCommentClick(reviewId) },
                        isLiked = state.selectedReview?.liked ?: false
                    )

                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                    Text(
                        text = "Comments",
                        color =  MaterialTheme.colorScheme.onPrimary,
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

        IconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
