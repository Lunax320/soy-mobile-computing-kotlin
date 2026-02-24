package com.example.soymusicreviewapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.data.Review
import com.example.soymusicreviewapp.ui.data.Song
import kotlin.Boolean

@Composable
fun FeedScreenHeader(
    currentTab: Int,
    modifier: Modifier = Modifier
) {
    val colorForYou: Int
    if (currentTab == 0) {
        colorForYou = R.color.azulcal
    } else {
        colorForYou = R.color.violetaClaro
    }

    val colorFollowing: Int
    if (currentTab == 1) {
        colorFollowing = R.color.azulcal
    } else {
        colorFollowing = R.color.violetaClaro
    }

    val colorLatest: Int
    if (currentTab == 2) {
        colorLatest = R.color.azulcal
    } else {
        colorLatest = R.color.violetaClaro
    }

    Box() {
        Image(
            painter = painterResource(R.drawable.bg_plain_top_v3),
            contentDescription = stringResource(R.string.top_background))

        Column(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Row() {
                Spacer(modifier = Modifier.width(9.dp))
                LogoSoy(modifier = Modifier.size(57.dp))
                Spacer(modifier = Modifier.width(12.dp))
                TextSoy(size = 45.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                GeneralButton(
                    text = "For you",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorForYou
                )
                GeneralButton(
                    text = "Social",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorFollowing
                )
                GeneralButton(
                    text = "Latest",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    fontSize = 14.sp,
                    color = colorLatest
                )
            }
        }
    }
}

@Composable
fun ReviewCard(
    review: Review,
    modifier: Modifier = Modifier,
    isProfileView: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.vclaro)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = review.userImageId),
                    contentDescription = "User image",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    UserText(user = review.userName)
                    Spacer(modifier = Modifier.height(5.dp))
                    DateText(date = review.date)
                }
            }

            if (isProfileView) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit review",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Delete review",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SongText(songName = review.songName)
            ArtistText(artistName = review.userName)
            Spacer(modifier = Modifier.height(10.dp))
            RatingText(rating = review.rating)
            Spacer(modifier = Modifier.height(10.dp))
            ReviewText(review = review.reviewText)
            Spacer(modifier = Modifier.height(7.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = "Like",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "42", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_comment),
                contentDescription = "Comment",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Comment", color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun ReviewList(
    reviews: List<Review>,
    title: String,
    modifier: Modifier = Modifier,
    isProfileView: Boolean = false

) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }

        items(reviews.size) { index ->
            ReviewCard(
                review = reviews[index],
                modifier = Modifier.fillMaxWidth(),
                isProfileView = isProfileView
            )
        }
    }
}

@Composable
fun SongCard(
    song: Song,
    modifier: Modifier = Modifier,
    isNewRelease: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.vclaro)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = song.imageId),
                contentDescription = "Song cover",
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(15.dp))

            Column {
                SongText(songName = song.name)

                Spacer(modifier = Modifier.height(5.dp))

                ArtistText(artistName = song.artist)

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    if (isNewRelease) {
                        GenreTag("New", backgroundColor = R.color.amarilloOscuro, borderColor = R.color.amarilloClaro)
                    } else {
                        GenreTag(song.genre)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = song.duration,
                        color = colorResource(R.color.vclaroletra),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SongList(
    songs: List<Song>,
    modifier: Modifier = Modifier,
    isNewRelease: Boolean = false
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(songs.size) { index ->
            SongCard(
                song = songs[index],
                modifier = Modifier.fillMaxWidth(),
                isNewRelease = isNewRelease
            )
        }
    }
}

@Composable
fun FooterScreen(){
    Box(
        modifier = Modifier
    ){
        Image(
            painter = painterResource(R.drawable.bg_plain_bottom),
            contentDescription = stringResource(R.string.bottom_background),
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            BottomIcon(
                modifier = Modifier,
                imageId = R.drawable.ic_home,
                descriptionId = (R.string.home_icon),
                iconNameId = (R.string.home)
            )
            Spacer(modifier = Modifier.width(10.dp))

            BottomIcon(
                modifier = Modifier,
                imageId = R.drawable.ic_search,
                descriptionId = (R.string.search_icon),
                iconNameId = (R.string.explore)
            )
            Spacer(modifier = Modifier.width(10.dp))

            BottomIcon(
                modifier = Modifier,
                imageId = R.drawable.ic_create,
                descriptionId = (R.string.create_icon),
                iconNameId = (R.string.create)
            )
            Spacer(modifier = Modifier.width(10.dp))

            BottomIcon(
                modifier = Modifier,
                imageId = R.drawable.ic_notification,
                descriptionId = R.string.notification_icon,
                iconNameId = R.string.notifications
            )
            Spacer(modifier = Modifier.width(10.dp))

            BottomIcon(
                modifier = Modifier,
                imageId = R.drawable.ic_profile,
                descriptionId = R.string.profile_icon,
                iconNameId = R.string.profile
            )
        }
    }
}