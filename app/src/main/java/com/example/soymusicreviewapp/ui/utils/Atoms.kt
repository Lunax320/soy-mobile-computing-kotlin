package com.example.soymusicreviewapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R

@Composable
fun TextSoy(
    modifier: Modifier = Modifier,
    size: TextUnit
) {
    Text(
        text = stringResource(R.string.soy),
        fontSize = size,
        fontFamily = FontFamily.Cursive,
        color = Color.White,
        modifier = modifier
    )
}

@Composable
fun LogoSoy(
    modifier: Modifier = Modifier.size(250.dp)
) {
    Image(
        painter = painterResource(R.drawable.img_logo_soy),
        contentDescription = stringResource(R.string.logo_soy),
        modifier = modifier
    )
}

@Composable
fun GeneralButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(vertical = 8.dp),
    text: String,
    fontSize: TextUnit = 20.sp,
    color: Int = R.color.violetaClaro,
    shape: Shape = RoundedCornerShape(8.dp),
    onClick : () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(color),
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
        label = { Text(text = stringResource(id = labelId), color = Color.White) },
        visualTransformation = visualTransformation,
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 8.dp)
            .border(
                width = 2.dp,
                color = colorResource(R.color.violetaApagado),
                shape = shape
            )
            .background(
                color = colorResource(R.color.azul2),
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
            focusedTextColor = colorResource(R.color.vclaroletra),
            unfocusedTextColor = colorResource(R.color.vclaroletra),
        )
    )
}

@Composable
fun PlainBackground(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(R.drawable.bg_plain),
        contentDescription = stringResource(R.string.main_screen_background),
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
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
    placeholderId: Int = (R.string.search_placeholder),
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
                color = colorResource(R.color.violetaClaro),
                shape = shape
            )
            .background(
                color = colorResource(R.color.azul2),
                shape = shape
            ),

        placeholder = {
            Text(
                text = stringResource(placeholderId),
                color = Color.White.copy(alpha = 0.5f)
            )
        },

        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
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
        color = Color.White,
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
        color = colorResource(R.color.vclaroletra),
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun SongText(
    modifier: Modifier = Modifier,
    songName: String
) {
    Text(
        text = songName,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = modifier
    )
}

@Composable
fun ArtistText(
    modifier: Modifier = Modifier,
    artistName: String
) {
    Text(
        text = artistName,
        color = colorResource(R.color.vclaroletra),
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
        color = Color.Yellow,
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
        color = Color.White,
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun GenreTag(
    name: String,
    borderColor: Int = R.color.violetaClaro,
    backgroundColor: Int = R.color.grisClaro,
    modifier: Modifier = Modifier
) {
    Surface(
        color = colorResource(backgroundColor),
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
            .border(
                width = 1.dp,
                color = colorResource(borderColor),
                shape = RoundedCornerShape(50.dp),
            )
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}