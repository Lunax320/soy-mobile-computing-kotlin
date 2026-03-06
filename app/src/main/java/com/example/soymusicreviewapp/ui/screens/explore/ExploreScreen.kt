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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.TopPlainBackground
import com.example.soymusicreviewapp.ui.utils.SoyBackground
import com.example.soymusicreviewapp.ui.utils.SearchBar
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.SongCard
import com.example.soymusicreviewapp.ui.utils.SongList

// Component for filtering genres (kept as is)
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
                color = colorResource(R.color.azulcal),
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.synth_pop),
                color = colorResource(R.color.azulcal),
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.synthwave),
                color = colorResource(R.color.azulcal),
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.dream_pop),
                color = colorResource(R.color.azulcal),
                fontSize = 14.sp
            )
        }
        item {
            GeneralButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(R.string.indie_pop),
                color = colorResource(R.color.azulcal),
                fontSize = 14.sp
            )
        }
    }
}

// Header component receiving state and event handler
@Composable
fun ExploreScreenHeader(
    searchValue: String,
    onSearchChange: (String) -> Unit,
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
                    color = colorResource(R.color.azulcal),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                GeneralButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.artists),
                    color = colorResource(R.color.violetaClaro),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            // SearchBar now controlled by parameters
            SearchBar(
                currentValue = searchValue,
                onValueChanged = onSearchChange,
            )

            Spacer(modifier = Modifier.height(9.dp))

            GenresFilter(modifier = Modifier)

        }
    }
}

// Body component displaying the song list from state
@Composable
fun ExploreScreenBody(
    songs: List<Song>,
    onSongClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        SoyBackground()

        SongList(
            songs = songs,
            modifier = Modifier.fillMaxSize(),
            isNewRelease = false,
            onSongClick = onSongClick
        )
    }
}

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onSongClick: (Int) -> Unit,
    viewModel: ExploreViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Pass search state to header
        ExploreScreenHeader(
            searchValue = state.searchText,
            onSearchChange = { viewModel.onSearchChange(it) }
        )
        // Pass songs list to body
        ExploreScreenBody(
            songs = state.songs,
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

@Composable
@Preview
fun TopBackgroundPreview() {
    CompMovilProyectoTheme {
        TopPlainBackground()
    }
}

@Composable
@Preview
fun GeneralButtonPreview() {
    CompMovilProyectoTheme {
        GeneralButton(
            modifier = Modifier, text = "Artist",
            color = colorResource(R.color.azulcal)
        )
    }
}

@Composable
@Preview
fun SearchBarPreview() {
    CompMovilProyectoTheme {
        SearchBar(
            currentValue = "",
            onValueChanged = {},
        )
    }
}

@Composable
@Preview
fun GenresFilterPreview() {
    CompMovilProyectoTheme {
        GenresFilter(modifier = Modifier)
    }
}

@Composable
@Preview
fun ExploreScreenHeaderPreview() {
    CompMovilProyectoTheme {
        ExploreScreenHeader(
            searchValue = "",
            onSearchChange = {}
        )
    }
}

@Composable
@Preview
fun ExploreScreenBodyPreview() {
    CompMovilProyectoTheme {
        ExploreScreenBody(
            songs = emptyList(),
            onSongClick = {}
        )
    }
}

@Preview
@Composable
fun ExploreScreenPreview() {
    CompMovilProyectoTheme {
        ExploreScreen(
            onSongClick = {}
        )
    }
}