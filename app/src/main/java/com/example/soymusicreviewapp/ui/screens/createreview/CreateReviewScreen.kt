package com.example.soymusicreviewapp.ui.screens.createreview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.BackButton
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.SongCard
import com.example.soymusicreviewapp.ui.utils.SoyBackground
import com.example.soymusicreviewapp.ui.utils.TopPlainBackground

@Composable
fun CreateReviewScreen(
    songId: Int,
    onBackClick: () -> Unit,
    viewModel: CreateReviewViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val song = viewModel.getSong(songId)

    Box(modifier = modifier.fillMaxSize()) {
        SoyBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            CreateReviewHeader(onBackClick = onBackClick)

            if (song != null) {
                CreateReviewBody(
                    song = song,
                    reviewText = state.reviewText,
                    rating = state.rating,
                    onReviewChange = { viewModel.onReviewTextChange(it) },
                    onRatingChange = { viewModel.onRatingChange(it) },
                    onSubmitClick = { /* Lógica de guardado futura */ },
                    modifier = Modifier.weight(1f)
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error loading song data", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun CreateReviewHeader(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(com.example.soymusicreviewapp.R.drawable.bg_plain_top_v2),
            contentDescription = stringResource(R.string.background_plain_top_type_2)
        )
        Column(modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)) {
            BackButton(onBack = onBackClick)
            Text(
                text = "Create Review",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 20.dp)
            )
        }
    }
}

@Composable
fun CreateReviewBody(
    song: Song,
    reviewText: String,
    rating: Int,
    onReviewChange: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        SelectedSongSection(song = song)

        Spacer(modifier = Modifier.height(16.dp))

        RatingSelectionCard(rating = rating, onRatingChange = onRatingChange)

        Spacer(modifier = Modifier.height(16.dp))

        ReviewInputCard(reviewText = reviewText, onReviewChange = onReviewChange)

        Spacer(modifier = Modifier.height(65.dp))

        GeneralButton(
            text = "Publish Review",
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(70.dp)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun SelectedSongSection(song: Song, modifier: Modifier = Modifier) {
    SongCard(
        song = song,
        onClick = { }
    )
}
@Composable
fun RatingSelectionCard(rating: Int, onRatingChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceDim,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(horizontal = 18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Rating",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 1..5) {
                    IconButton(onClick = { onRatingChange(i) }) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star $i",
                            tint = if (i <= rating) MaterialTheme.colorScheme.secondary else Color.Gray.copy(alpha = 0.3f),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewInputCard(reviewText: String, onReviewChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceDim,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(horizontal = 18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Your opinion",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = onReviewChange,
                placeholder = {
                    Text("Share your opinion about this song...", color = Color.Gray)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                    unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Text(
                text = "${reviewText.length}/500 characters",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun CreateReviewScreenPreview() {
    CompMovilProyectoTheme {
        val songId = LocalSongsProvider.songs.first().songId

        CreateReviewScreen(
            songId = songId,
            viewModel = viewModel(),
            onBackClick = {}
        )
    }
}
