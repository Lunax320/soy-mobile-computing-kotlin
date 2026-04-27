package com.example.soymusicreviewapp.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.soymusicreviewapp.ui.utils.EditButton

@Composable
fun ProfileScreen(
    userId: String,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    settingsButtonPressed: () -> Unit,
    onEditReview: (String, String) -> Unit,
    onEditProfileClick: () -> Unit = {},
    onReviewClick: (String) -> Unit = {},
    onUserClick: (String) -> Unit = {},
    onFollowersClick: (String) -> Unit = {},
    onFollowingClick: (String) -> Unit = {},
    onCommentClick: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    var localUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            localUri = uri
            viewModel.uploadImageToFirebase(uri)
        }
    }

    LaunchedEffect(userId) {
        viewModel.loadUserProfile(userId)
    }

    Column(modifier = modifier.fillMaxSize()) {
        ProfileScreenHeader(
            profileImageUrl = localUri ?: state.user.profileImageUrl,
            name = state.user.name,
            username = state.user.username,
            reviewCount = state.reviewCount,
            followersCount = state.user.followersCount,
            followingCount = state.user.followingCount,
            settingsButtonPressed = settingsButtonPressed,
            onEditProfileClick = onEditProfileClick,
            editProfileClick = { launcher.launch("image/*") },
            isOwnProfile = userId == state.currentUserId,
            followed = state.user.followed,
            onFollowClick = { viewModel.followOrUnfollowUser(userId) },
            onFollowersClick = { onFollowersClick(userId) },
            onFollowingClick = { onFollowingClick(userId) }
        )

        ProfileScreenBody(
            userReviews = state.userReviews,
            currentUserId = state.currentUserId,
            onDeleteClick = { reviewId -> viewModel.deleteReview(reviewId) },
            onEditClick = { review -> onEditReview(review.id, review.songId) },
            onReviewClick = onReviewClick,
            onUserClick = onUserClick,
            onCommentClick = onCommentClick,
            onLikeClick = { reviewId -> viewModel.sendOrDeleteReviewLike(reviewId, state.currentUserId) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ProfileScreenHeader(
    modifier: Modifier = Modifier,
    profileImageUrl: Any?,
    name: String,
    username: String,
    reviewCount: Int,
    followersCount: Int,
    followingCount: Int,
    settingsButtonPressed: () -> Unit,
    onEditProfileClick: () -> Unit,
    editProfileClick: () -> Unit,
    isOwnProfile: Boolean,
    followed: Boolean,
    onFollowClick: () -> Unit,
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {},
    onImageClick: () -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        TopPlainBackground()

        if (isOwnProfile) {
            EditButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                onClick = onEditProfileClick
            )
        }

        SettingsButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            onClick = settingsButtonPressed
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfilePictureWithAction(
                model = profileImageUrl,
                isOwnProfile = isOwnProfile,
                followed = followed,
                onEditClick = editProfileClick,
                onFollowClick = onFollowClick,
                onClick = onImageClick,
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = name,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = username,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Row {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = reviewCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(55.dp))
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clickable { onFollowersClick() },
                    text = followersCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clickable { onFollowingClick() },
                    text = followingCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.reviews),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(40.dp))
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { onFollowersClick() },
                    text = stringResource(R.string.followers),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(40.dp))
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { onFollowingClick() },
                    text = stringResource(R.string.following),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfilePictureWithAction(
    model: Any?,
    isOwnProfile: Boolean,
    followed: Boolean,
    onEditClick: () -> Unit,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
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
                .memoryCacheKey(model?.toString())
                .build(),
            contentDescription = stringResource(R.string.profile),
            error = painterResource(id = R.drawable.ic_profile),
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

        if (isOwnProfile) {
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
        } else {
            FilledIconButton(
                onClick = onFollowClick,
                modifier = Modifier
                    .size(buttonSize)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (followed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = if (followed) Icons.Filled.Remove else Icons.Filled.Add,
                    contentDescription = if (followed) "Unfollow user" else "Follow user",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileScreenBody(
    userReviews: List<Review>,
    currentUserId: String,
    onDeleteClick: (String) -> Unit,
    onEditClick: (Review) -> Unit,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCommentClick: (String) -> Unit,
) {
    Box(modifier = modifier) {
        PlainBackground()
        ReviewList(
            reviews = userReviews,
            currentUserId = currentUserId,
            onReviewClick = onReviewClick,
            onUserClick = onUserClick,
            onLikeClick = onLikeClick,
            modifier = Modifier.fillMaxSize(),
            title = "Publicaciones",
            isProfileView = true,
            onCommentClick = onCommentClick,
            onDeleteClick = onDeleteClick,
            onEditClick = { id : String ->
                val review = userReviews.find { it.id == id }
                if (review != null) {
                    onEditClick(review)
                }
            }
        )
    }
}

@Composable
@Preview
fun ProfileScreenHeaderPreview() {
    CompMovilProyectoTheme {
        ProfileScreenHeader(
            profileImageUrl = null,
            name = "Music Lover",
            username = "@musiclover",
            reviewCount = 2,
            followersCount = 234,
            followingCount = 189,
            settingsButtonPressed = {},
            onEditProfileClick = {},
            editProfileClick = {},
            isOwnProfile = true,
            followed = false,
            onFollowClick = {}
        )
    }
}
