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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewInfo

@Composable
fun CommentReviewScreen(
    reviewId: String,
    onCloseClick: () -> Unit,
    viewModel: CommentReviewViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val parentReview = state.parentReview
    val comments = state.comments

    Box(modifier = modifier.fillMaxSize()) {
        PlainBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            CommentReviewHeader(
                songName = parentReview?.songName ?: "Cargando...",
                onCloseClick = onCloseClick
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            // TODO EL CONTENIDO DENTRO DE LAZYCOLUMN
            Box(modifier = Modifier.weight(1f)) {
                when {
                    state.isLoading && parentReview == null && comments.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                    else -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            if (parentReview != null) {
                                item(key = "parent_review") {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceDim
                                        )
                                    ) {
                                        ReviewInfo(
                                            review = parentReview,
                                            currentUserId = state.currentUserId,
                                            isProfileView = false,
                                            onDeleteClick = {},
                                            onEditClick = {},
                                            onUserClick = {},
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }
                            }

                            if (comments.isNotEmpty()) {
                                item(key = "comments_header") {
                                    Text(
                                        text = "Comentarios (${comments.size})",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }

                            items(
                                count = comments.size,
                                key = { index -> comments[index].id }
                            ) { index ->
                                ReviewInfo(
                                    review = comments[index],
                                    currentUserId = state.currentUserId,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    onDeleteClick = { /* TODO: eliminar comentario */ },
                                    onEditClick = { /* TODO: editar comentario */ },
                                    onUserClick = {}
                                )
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    modifier = Modifier.padding(start = 72.dp)
                                )
                            }

                            if (comments.isEmpty() && !state.isLoading && parentReview != null) {
                                item(key = "empty_state") {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "No comments yet.",
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Be the first to comment!",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            state.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            CommentInputBar(
                commentText = state.commentText,
                onCommentChange = { viewModel.onCommentTextChange(it) },
                onSendClick = { viewModel.sendComment() },
                isSending = state.isSending
            )
        }
    }
}

@Composable
fun CommentReviewHeader(
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
                fontSize = 14.sp,
                maxLines = 1
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
fun CommentInputBar(
    commentText: String,
    onCommentChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = commentText,
                onValueChange = onCommentChange,
                placeholder = { Text("Write a comment...", color = Color.Gray) },
                enabled = !isSending,
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
                enabled = !isSending && commentText.isNotBlank(),
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Comment",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${commentText.length}/500 characters",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}