package com.example.soymusicreviewapp.ui.screens.reviewsmap

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soymusicreviewapp.ui.utils.BackButton
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ReviewsMapScreen(
    onBackClick: () -> Unit,
    viewModel: ReviewsMapViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val bogota = LatLng(4.6097, -74.0817)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bogota, 11f)
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            state.reviews.forEach { review ->
                if (review.latitude != null && review.longitude != null) {
                    Marker(
                        state = MarkerState(position = LatLng(review.latitude, review.longitude)),
                        title = "${review.userName} - ${review.songName}",
                        snippet = "${review.rating}⭐: ${review.reviewText}"
                    )
                }
            }
        }

        Box(modifier = Modifier.padding(top = 45.dp, start = 16.dp)) {
            BackButton(onBack = onBackClick)
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        state.errorMessage?.let {
            Text(
                text = it,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}