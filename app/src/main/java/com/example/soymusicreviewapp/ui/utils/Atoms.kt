package com.example.soymusicreviewapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme

@Composable
fun TextSoy(
    modifier: Modifier = Modifier,
    size: TextUnit
) {
    Text(
        text = stringResource(R.string.soy),
        fontSize = size,
        fontFamily = FontFamily.Cursive,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    )
}

@Composable
fun LogoSoy(
    modifier: Modifier = Modifier.size(250.dp)
) {
    if (LocalInspectionMode.current) {
        Box(modifier = modifier.background(Color.Gray.copy(alpha = 0.2f)))
    } else {
        Image(
            painter = painterResource(R.drawable.img_logo_soy),
            contentDescription = stringResource(R.string.logo_soy),
            modifier = modifier
        )
    }
}

@Composable
fun GeneralButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(vertical = 8.dp),
    text: String,
    fontSize: TextUnit = 20.sp,
    color: Color = MaterialTheme.colorScheme.secondary,
    shape: Shape = RoundedCornerShape(8.dp),
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
        ),
        shape = shape
    ) {
        Text(
            text = text,
            fontSize = fontSize
        )
    }
}


@Composable
fun GeneralForm(
    labelId: Int,
    textValue: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    isPassword: Boolean = false
) {

    val visualTransformation = if (isPassword) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    TextField(
        value = textValue,
        onValueChange = onValueChanged,
        label = { Text(text = stringResource(id = labelId), color = MaterialTheme.colorScheme.onPrimary) },
        visualTransformation = visualTransformation,
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 8.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = shape
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = shape
            ),
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    )
}

@Composable
fun PlainBackground(
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) {
        Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
    } else {
        Image(
            painter = painterResource(R.drawable.bg_plain),
            contentDescription = stringResource(R.string.main_screen_background),
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun TopPlainBackground(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(R.drawable.bg_plain_top),
        contentDescription = stringResource(R.string.top_plain_background),
        modifier = modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun SoyBackground(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(R.drawable.bg_soy),
        contentDescription = stringResource(R.string.soy_screen_background),
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    currentValue: String,
    onValueChanged: (String) -> Unit,
    placeholderId: Int = R.string.search_placeholder,
    shape: Shape = RoundedCornerShape(8.dp)
) {

    TextField(
        value = currentValue,
        onValueChange = onValueChanged,

        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = shape
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = shape
            ),

        placeholder = {
            Text(
                text = stringResource(placeholderId),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
            )
        },

        shape = shape,

        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
fun UserText(
    modifier: Modifier = Modifier,
    user: String,
) {
    Text(
        text = user,
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun DateText(
    modifier: Modifier = Modifier,
    date: String
) {
    Text(
        text = date,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun SongText(
    modifier: Modifier = Modifier,
    songName: String,
    fontSize: TextUnit = 16.sp
) {
    Text(
        text = songName,
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        modifier = modifier
    )
}

@Composable
fun ArtistText(
    modifier: Modifier = Modifier,
    artistName: String,
    fontSize: TextUnit = 14.sp
) {
    Text(
        text = artistName,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontSize = fontSize,
        modifier = modifier
    )
}

@Composable
fun RatingText(
    modifier: Modifier = Modifier,
    rating: Int
) {
    Text(
        text = "⭐".repeat(rating) + " $rating/5",
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = modifier
    )
}

@Composable
fun ReviewText(
    modifier: Modifier = Modifier,
    review: String
) {
    Text(
        text = review,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun GenreTag(
    name: String,
    borderColor: Color = MaterialTheme.colorScheme.secondary,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiary,
    tagSize: TextUnit = 12.sp,
    modifier: Modifier = Modifier
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50.dp),
            )
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = tagSize,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "Settings",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun BackButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton( modifier = modifier,onClick = onBack) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Arrow back",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

// Preview


@Composable
@Preview
fun GeneralButtonPreview() {
    CompMovilProyectoTheme {
        GeneralButton(
            text = "Login",
            onClick = {}
        )
    }
}

@Composable
@Preview
fun GeneralFormPreview() {
    CompMovilProyectoTheme {
        GeneralForm(
            labelId = R.string.user,
            textValue = "",
            onValueChanged = {}
        )
    }
}
@Preview
@Composable
fun SearchBarPreview(){
    CompMovilProyectoTheme {
        SearchBar(
            currentValue = "",
            onValueChanged = {}
        )
    }
}
