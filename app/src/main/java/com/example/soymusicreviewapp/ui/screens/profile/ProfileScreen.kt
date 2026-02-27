package com.example.soymusicreviewapp.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.TopPlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewList
import com.example.soymusicreviewapp.ui.utils.SettingsButton

@Composable
fun ProfileScreenHeader(
    modifier: Modifier = Modifier,
    settingsButtonPressed: () -> Unit
){
    Box(
        contentAlignment = Alignment.Center
    ) {
        TopPlainBackground()
        SettingsButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            onClick = settingsButtonPressed
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            ProfileImage(
                imageId = R.drawable.img_avatar_penguin,
                descriptionId = (R.string.profile_photo)
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(R.string.music_lover),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = stringResource(R.string.musiclover),
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Row{
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(R.string._2),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(55.dp))
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(R.string._234),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(R.string._189),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

            }
            Column{
                Row{
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(R.string.reviews),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(R.string.followers),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(R.string.following),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imageId: Int,
    descriptionId: Int,
) {
    Image(
        painter = painterResource(imageId),
        contentDescription = stringResource(descriptionId),
        modifier = modifier
            .size(125.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = colorResource(R.color.violetaClaro),
                shape = RoundedCornerShape(20.dp)
            )
    )
}



@Composable
fun ProfileScreenBody(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        PlainBackground()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val allReviews = LocalReviewProvider.reviews
            ReviewList(
                reviews = allReviews,
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.my_reviews),
                isProfileView = true
            )
        }
    }
}


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    settingsButtonPressed: () -> Unit
                 ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ProfileScreenHeader(
            settingsButtonPressed = settingsButtonPressed
        )
        ProfileScreenBody(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}


//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Composable
@Preview
fun ProfileImagePreview() {
    ProfileImage(
        imageId = R.drawable.img_avatar_penguin,
        descriptionId = R.string.profile_photo
    )
}

@Composable
@Preview
fun ProfileScreenHeaderPreview() {
    ProfileScreenHeader(
        settingsButtonPressed = {}
    )
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(
        settingsButtonPressed = {}
    )
}

@Composable
@Preview
fun ProfileScreenBodyPreview() {
    ProfileScreenBody()
}
