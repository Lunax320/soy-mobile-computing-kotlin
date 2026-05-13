package com.example.soymusicreviewapp.ui.screens.createreview

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.BackButton
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.SongCard
import com.example.soymusicreviewapp.ui.utils.SoyBackground
import com.google.android.gms.location.LocationServices

@Composable
fun CreateReviewScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReviewViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Para localizacion
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (isGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        viewModel.updateLocation(location.latitude, location.longitude)
                    }
                    viewModel.saveReview()
                }.addOnFailureListener { viewModel.saveReview() }
            } catch (_: SecurityException) { viewModel.saveReview() }
        } else {
            viewModel.saveReview()
        }
    }

    val onPublishClick: () -> Unit = {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        viewModel.updateLocation(location.latitude, location.longitude)
                    }
                    viewModel.saveReview()
                }.addOnFailureListener { viewModel.saveReview() }
            } catch (_: SecurityException) { viewModel.saveReview() }
        } else {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    LaunchedEffect(state.navigateBack) {
        if (state.navigateBack) {
            onBackClick()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        SoyBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            CreateReviewHeader(onBackClick = onBackClick)

            state.song?.let { song ->
                CreateReviewBody(
                    song = song,
                    reviewText = state.reviewText,
                    rating = state.rating,
                    isLoading = state.isLoading,
                    isFavorite = state.isFavorite,
                    onReviewChange = { viewModel.onReviewTextChanged(it) },
                    onRatingChange = { viewModel.onRatingChanged(it) },
                    onFavoriteClick = { /* TODO */ },
                    onSubmitClick = onPublishClick,
                    modifier = Modifier.weight(1f)
                )
            } ?: Box(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            }
        }

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
            painter = painterResource(R.drawable.bg_plain_top_v2),
            contentDescription = stringResource(R.string.background_plain_top_type_2)
        )
        Column(modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)) {
            BackButton(onBack = onBackClick)
            Text(
                text = stringResource(R.string.create_review),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )
        }
    }
}

@Composable
fun CreateReviewBody(
    song: Song,
    reviewText: String,
    rating: Int,
    isLoading: Boolean,
    isFavorite: Boolean,
    onReviewChange: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(17.dp))

        SelectedSongSection(
            song = song,
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        RatingSelectionCard(rating = rating, onRatingChange = onRatingChange)

        Spacer(modifier = Modifier.height(12.dp))

        ReviewInputCard(reviewText = reviewText, onReviewChange = onReviewChange)

        Spacer(modifier = Modifier.height(16.dp))

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
fun SelectedSongSection(
    song: Song,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    SongCard(
        song = song,
        onClick = { },
        isFavorite = isFavorite,
        onFavoriteClick = onFavoriteClick,
        modifier = modifier.padding(horizontal = 3.dp)
    )
}

@Composable
fun RatingSelectionCard(rating: Int, onRatingChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceDim,
        shape = RoundedCornerShape(12.dp),
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
        shape = RoundedCornerShape(12.dp),
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

@Preview(showBackground = true)
@Composable
fun CreateReviewScreenPreview() {
    CompMovilProyectoTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            SoyBackground()
            Column(modifier = Modifier.fillMaxSize()) {
                CreateReviewHeader(onBackClick = {})
                CreateReviewBody(
                    song = LocalSongsProvider.songs[0],
                    reviewText = "",
                    rating = 0,
                    isLoading = false,
                    isFavorite = false,
                    onReviewChange = {},
                    onRatingChange = {},
                    onFavoriteClick = {},
                    onSubmitClick = {}
                )
            }
        }
    }
}
