package com.example.soymusicreviewapp.ui.screens.createreview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun CreateReviewScreen(
    songId: String,
    onBackClick: () -> Unit,
    viewModel: CreateReviewViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val song = viewModel.getSong(songId)

    // Lógica de navegación reactiva
    LaunchedEffect(state.navigateBack) {
        if (state.navigateBack) {
            onBackClick()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        SoyBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            // 1. El Header siempre arriba
            CreateReviewHeader(onBackClick = onBackClick)

            // 2. El Cuerpo solo si la canción existe
            if (song != null) {
                CreateReviewBody(
                    song = song,
                    reviewText = state.reviewText,
                    rating = state.rating,
                    isLoading = state.isLoading, // <-- Esto es lo que causaba el rojo
                    onReviewChange = { viewModel.onReviewTextChange(it) },
                    onRatingChange = { viewModel.onRatingChange(it) },
                    onSubmitClick = { viewModel.createReview(songId) },
                    modifier = Modifier.weight(1f) // Esto empuja el contenido
                )
            }
            else {
                // Mensaje opcional por si no carga la canción
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading song data...", color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }

        // 3. El mensaje de error flotando al fondo (fuera de la Column para que no se mueva)
        if (state.errorMessage != null) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = state.errorMessage ?: "Error publicando",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
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
    isLoading: Boolean, // <-- AGREGAR ESTO
    onReviewChange: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        SelectedSongSection(song = song)

        Spacer(modifier = Modifier.height(16.dp))

        RatingSelectionCard(rating = rating, onRatingChange = onRatingChange)

        Spacer(modifier = Modifier.height(16.dp))

        ReviewInputCard(reviewText = reviewText, onReviewChange = onReviewChange)

        Spacer(modifier = Modifier.height(40.dp))

        // Tu botón usando el estado de carga
        GeneralButton(
            text = if (isLoading) "Publishing..." else "Publish Review",
            color = if (isLoading) Color.Gray else MaterialTheme.colorScheme.secondary,
            onClick = {
                if (!isLoading) onSubmitClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(70.dp)
                .padding(bottom = 20.dp)
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