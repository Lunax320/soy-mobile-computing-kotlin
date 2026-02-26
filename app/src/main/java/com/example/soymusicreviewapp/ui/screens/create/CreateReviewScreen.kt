package com.example.soymusicreviewapp.ui.screens.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import com.example.soymusicreviewapp.ui.utils.SoyBackground
import com.example.soymusicreviewapp.ui.utils.SearchBar
import com.example.soymusicreviewapp.ui.utils.SongList

@Composable
fun CreateReviewScreenHeader(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bg_plain_top_v2),
            contentDescription = stringResource(R.string.background_plain_top_type_2))

        Column {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.left_arrow),
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.create_review),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
fun CreateReviewScreenBody(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val allSongs = LocalSongsProvider.songs

    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bg_soy_top),
            contentDescription = "Top background",
            modifier = Modifier.fillMaxWidth()
        )

        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.search_song),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 25.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            SearchBar(
                modifier = Modifier.padding(horizontal = 24.dp),
                currentValue = "",
                onValueChanged = {}
            )

            Box(modifier = modifier) {
                SoyBackground()

                SongList(
                    songs = allSongs,
                    modifier = Modifier.fillMaxSize(),
                    isNewRelease = false // For the review, the musical genre is shown
                )
            }

        }
    }
}

@Composable
fun CreateReviewScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CreateReviewScreenHeader()
        CreateReviewScreenBody(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun CreateReviewScreenPreview() {
    CreateReviewScreen()
}