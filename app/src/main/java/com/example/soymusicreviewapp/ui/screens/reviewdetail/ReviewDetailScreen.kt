package com.example.soymusicreviewapp.ui.screens.reviewdetail

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewInfo

// Main screen composable
@Composable
fun ReviewDetailScreen(
    reviewId: Int,
    modifier: Modifier = Modifier,
    viewModel: ReviewDetailViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(reviewId) {
        viewModel.loadReview(reviewId)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        PlainBackground()

        // Only show content if the review was found
        if (state.selectedReview != null) {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                item {
                    // Use the review from the state
                    ReviewInfo(
                        review = state.selectedReview!!,
                        isProfileView = true
                    )

                    HorizontalDivider(thickness = 1.dp, color =  MaterialTheme.colorScheme.tertiary)

                    ReviewActionBar(
                        onLike = { /* */ },
                        onComment = { /* */ },
                        onShare = { /* */ },
                        onFavorite = { /* */ },
                        isLiked = false
                    )

                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                    Text(
                        text = "Most relevant reviews",
                        color =  MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp)
                    )
                }

                items(state.responseReviews.size) { index ->
                    ReviewInfo(
                        review = state.responseReviews[index],
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onTertiary)
                }
            }
        } else {
        }
    }
}

// Action bar component
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
                contentDescription = "Comment",
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