package com.example.soymusicreviewapp.ui.screens.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
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

        Column (
            modifier = Modifier
                .fillMaxWidth().padding(top = 40.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = stringResource(R.string.create_review),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
fun CreateReviewScreenBody(
    modifier: Modifier = Modifier,
    onSongClick: (Int) -> Unit,
) {
    val allSongs = LocalSongsProvider.songs
    var searchText by remember { mutableStateOf("")}

    Box(modifier = modifier) {
        SoyBackground()
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.search_song),
                color = MaterialTheme.colorScheme.onPrimary, 
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 25.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            SearchBar(
                modifier = Modifier.padding(horizontal = 24.dp),
                currentValue =searchText,
                onValueChanged = { searchText = it}
            )
            SongList(
                songs = allSongs, 
                onSongClick = onSongClick,
                modifier = Modifier.fillMaxSize(),
                isNewRelease = false // For the review, the musical genre is shown
            )
        }
    }
}

@Composable
fun CreateReviewScreen(
    onSongClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CreateReviewScreenHeader()
        CreateReviewScreenBody(
            onSongClick = onSongClick,
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
    CompMovilProyectoTheme {
        CreateReviewScreen(
            onSongClick = {}
        )
    }
}