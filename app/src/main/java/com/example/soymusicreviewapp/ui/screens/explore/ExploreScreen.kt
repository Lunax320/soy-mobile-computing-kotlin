package com.example.soymusicreviewapp.ui.screens.explore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import com.example.soymusicreviewapp.ui.utils.TopPlainBackground
import com.example.soymusicreviewapp.ui.utils.SoyBackground
import com.example.soymusicreviewapp.ui.utils.SearchBar
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.SongCard
import com.example.soymusicreviewapp.ui.utils.SongList

@Composable
fun GenresFilter(
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
    ) {
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.all),
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.electropop),
                color = R.color.violetaApagado,
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.synth_pop),
                color = R.color.violetaApagado,
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.synthwave),
                color = R.color.violetaApagado,
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.dream_pop),
                color = R.color.violetaApagado,
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.indie_pop),
                color = R.color.violetaApagado,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ExploreScreenHeader(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        TopPlainBackground()
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(R.string.explore_electropop),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                GeneralButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.songs),
                    color = R.color.azulcal,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                GeneralButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.artists),
                    color = R.color.violetaClaro,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            SearchBar(
                modifier = Modifier,
                currentValue = "",
                onValueChanged = {}
            )

            Spacer(modifier = Modifier.height(9.dp))

            GenresFilter(modifier = Modifier)

        }
    }
}

@Composable
fun ExploreScreenBody(
    modifier: Modifier = Modifier
) {
    val allSongs = LocalSongsProvider.songs

    Box(modifier = modifier) {
        SoyBackground()

        SongList(
            songs = allSongs,
            modifier = Modifier.fillMaxSize(),
            isNewRelease = false // For the exploration, the musical genre is shown
        )
    }
}

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ExploreScreenHeader()
        ExploreScreenBody(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------

@Composable
@Preview
fun TopBackgroundPreview() {
    TopPlainBackground()
}

@Composable
@Preview
fun GeneralButtonPreview() {
    GeneralButton(
        modifier = Modifier, text = "Artist",
        color = R.color.violetaApagado
    )
}

@Composable
@Preview
fun SearchBarPreview() {
    SearchBar(
        modifier = Modifier,
        currentValue = "",
        onValueChanged = {}
    )
}

@Composable
@Preview
fun SongCardPreview() {
    val example = LocalSongsProvider.songs[0]
    SongCard(
        song = example
    )
}

@Composable
@Preview
fun GenresFilterPreview() {
    GenresFilter(modifier = Modifier)
}

@Composable
@Preview
fun ExploreScreenHeaderPreview() {
    ExploreScreenHeader()
}

@Composable
@Preview
fun ExploreScreenBodyPreview() {
    ExploreScreenBody()
}

@Composable
@Preview
fun ExploreScreenPreview() {
    ExploreScreen()
}