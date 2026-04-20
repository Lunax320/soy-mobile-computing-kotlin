package com.example.soymusicreviewapp.ui.screens.songdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewList
import com.example.soymusicreviewapp.ui.utils.SongInfo

@Composable
fun SongsDetailScreen(
    songId: String,
    onBack: () -> Unit,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongsDetailViewModel
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(songId) {
        viewModel.loadData(songId)
    }

    Box(modifier = modifier.fillMaxSize()) {
        PlainBackground()

        if (state.selectedSong != null) {
            Column(modifier = Modifier.fillMaxSize()) {
                SongsDetailScreenHeader(
                    onBack = onBack,
                    songInfo = state.selectedSong!!
                )

                ReviewList(
                    reviews = state.reviews,
                    currentUserId = state.currentUserId,
                    onReviewClick = onReviewClick,
                    onUserClick = onUserClick,
                    onLikeClick = { reviewId -> viewModel.sendOrDeleteReviewLike(reviewId, state.currentUserId) },
                    title = stringResource(R.string.songs_reviews),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SongsDetailScreenHeader(
    onBack: () -> Unit,
    songInfo: Song
){
    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        Image(
            painter = painterResource(R.drawable.bg_plain_top_v3),
            contentDescription = stringResource(R.string.top_background),
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column (
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ){
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            SongInfo(
                song = songInfo,
                imageSize = 100.dp,
                titleSize = 20.sp,
                artistSize = 18.sp,
                tagSize = 16.sp,
                timeSize = 16.sp
            )
        }
    }
}

@Composable
@Preview
fun SongsDetailScreenPreview(){
    CompMovilProyectoTheme {
        val dummySongId = LocalSongsProvider.songs.firstOrNull()?.songId ?: "1"
        SongsDetailScreen(
            songId = dummySongId,
            onBack = {},
            onReviewClick = {},
            onUserClick = {},
            viewModel = viewModel()
        )
    }
}
