package com.example.soymusicreviewapp.ui.screens.commentreview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewInfo

@Composable
fun CommentReviewScreen(
    reviewId: String,
    onCloseClick: () -> Unit,
    viewModel: CommentReviewViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val parentReview = viewModel.getParentReview(reviewId)
    val comments = viewModel.getCommentsForReview(reviewId)

    Box(modifier = modifier.fillMaxSize()) {
        PlainBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            CommentReviewHeader(
                songName = parentReview?.songName ?: "Unknown Song",
                onCloseClick = onCloseClick,
                profileImageId = R.drawable.img_avatar_penguin
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))


            Box(modifier = Modifier.weight(1f)) {
                if (comments.isEmpty()) {
                    CommentEmptyState()
                } else {
                    CommentListSection(
                        comments = comments,
                        currentUserId = state.currentUserId
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            CommentInputBar(
                commentText = state.commentText,
                onCommentChange = { viewModel.onCommentTextChange(it) },
                onSendClick = { /* Logica para enviar el comentario */ }
            )
        }
    }
}

@Composable
fun CommentReviewHeader(
    profileImageId: Int,
    songName: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Comments",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = songName,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 14.sp
            )
        }

        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun CommentEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No comments yet.",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Be the first to comment!",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CommentListSection(
    comments: List<Review>,
    currentUserId: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(comments.size) { index ->
            ReviewInfo(
                review = comments[index],
                currentUserId = currentUserId, // Pasar el ID a ReviewInfo
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                onDeleteClick = { },
                onEditClick = { }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        }
    }
}

@Composable
fun CommentInputBar(
    commentText: String,
    onCommentChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            OutlinedTextField(
                value = commentText,
                onValueChange = onCommentChange,
                placeholder = {
                    Text("Write a comment...", color = Color.Gray)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
                    focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                    unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onSendClick,
                modifier = Modifier
                    .size(56.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Comment",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${commentText.length}/500 characters",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
