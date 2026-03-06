package com.example.soymusicreviewapp.ui.screens.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewList
import com.example.soymusicreviewapp.ui.utils.SongInfo


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
                    tint = Color.White
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
fun SongsDetailScreen(
    songInfo: Song,
    onBack: () -> Unit,
    responseReviews: List<Review>,
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        PlainBackground()
        Column(modifier = Modifier.fillMaxSize()) {
            SongsDetailScreenHeader(
                onBack = onBack,
                songInfo = songInfo
            )

            ReviewList(
                onReviewClick = onReviewClick,
                reviews = responseReviews,
                title = stringResource(R.string.songs_reviews),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
@Preview
fun SongsDetailScreenPreview(){
    CompMovilProyectoTheme {
        SongsDetailScreen(
            songInfo = LocalSongsProvider.songs[0],
            onBack = {},
            responseReviews = LocalReviewProvider.reviews,
            onReviewClick = {}
        )
    }
}
