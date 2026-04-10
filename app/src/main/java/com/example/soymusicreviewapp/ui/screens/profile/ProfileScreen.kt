package com.example.soymusicreviewapp.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.TopPlainBackground
import com.example.soymusicreviewapp.ui.utils.ReviewList
import com.example.soymusicreviewapp.ui.utils.SettingsButton

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    settingsButtonPressed: () -> Unit,
    onEditReview: (String, String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.uploadImageToFirebase(it) }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ProfileScreenHeader(
            profileImageId = state.profileImageId,
            profileImageUrl = state.profileImageUrl,
            name = state.name,
            username = state.username,
            reviewCount = state.reviewCount,
            followersCount = state.followersCount,
            followingCount = state.followingCount,
            settingsButtonPressed = settingsButtonPressed,
            editProfileClick = { launcher.launch("image/*") }
        )
        ProfileScreenBody(
            userReviews = state.userReviews,
            onDeleteClick = { reviewId -> viewModel.deleteReview(reviewId) },
            onEditClick = { review -> onEditReview(review.usernameId, review.songId) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ProfileScreenHeader(
    profileImageId: Int,
    modifier: Modifier = Modifier,
    profileImageUrl: String?,
    name: String,
    username: String,
    reviewCount: Int,
    followersCount: Int,
    followingCount: Int,
    settingsButtonPressed: () -> Unit,
    editProfileClick: () -> Unit
){
    Box(
        modifier = modifier,
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

            EditableProfilePicture(
                model = profileImageUrl,
                onEditClick = editProfileClick,
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = name,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = username,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Row{
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = reviewCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(55.dp))
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = followersCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = followingCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

            }
            Column{
                Row{
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(R.string.reviews),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(R.string.followers),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(R.string.following),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EditableProfilePicture(
    model: String?,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    avatarSize: Dp = 125.dp,
    buttonSize: Dp = 40.dp
) {
    Box(
        modifier = modifier.size(avatarSize),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(model)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.profile),
            error = painterResource(id = R.drawable.ic_loading),
            placeholder = painterResource(id = R.drawable.ic_profile),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(125.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(20.dp)
                )
        )


        FilledIconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(buttonSize)
                .align(Alignment.BottomEnd),
        ) {
            Icon(
                imageVector = Icons.Filled.PhotoCamera,
                contentDescription = stringResource(R.string.change_profile_picture),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ProfileScreenBody(
    userReviews: List<Review>,
    onDeleteClick: (String) -> Unit,
    onEditClick: (Review) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        PlainBackground()
        ReviewList(
            reviews = userReviews,
            modifier = Modifier.fillMaxSize(),
            title = stringResource(R.string.my_reviews),
            isProfileView = true,
            onDeleteClick = onDeleteClick,
            onEditClick = { id : String ->
                val review = userReviews.find { it.usernameId == id }
                review?.let { onEditClick(it) }
            }
        )
    }
}

@Composable
@Preview
fun ProfileScreenHeaderPreview() {
    CompMovilProyectoTheme {
        ProfileScreenHeader(
            profileImageId = R.drawable.img_avatar_penguin,
            profileImageUrl = null,
            name = "Music Lover",
            username = "@musiclover",
            reviewCount = 2,
            followersCount = 234,
            followingCount = 189,
            settingsButtonPressed = {},
            editProfileClick = {}
        )
    }
}