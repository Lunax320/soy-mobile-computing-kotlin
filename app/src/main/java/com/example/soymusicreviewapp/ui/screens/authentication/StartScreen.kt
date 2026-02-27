package com.example.soymusicreviewapp.ui.screens.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.LogoSoy

@Composable
fun StartScreenBody(
    modifier: Modifier = Modifier,
    loginButtonPressed: () -> Unit,
    registerButtonPressed: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 35.dp)
    ){
        Spacer(modifier = Modifier.height(115.dp))

        Text(
            text = stringResource(R.string.soy),
            color = Color.White,
            style = TextStyle(fontSize = 90.sp, fontFamily = FontFamily.Cursive)
        )

        Spacer(modifier = Modifier.height(6.dp))

        LogoSoy()

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.live_a_life_you_will_remember),
            color = Color.White,
            style = TextStyle(fontSize = 22.sp, fontStyle = FontStyle.Italic)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.avicii_the_nights),
            color = colorResource(R.color.violetaClaro),
            style = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        GeneralButton(
            text = stringResource(R.string.login),
            onClick = {
                loginButtonPressed()
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        GeneralButton(
            text = stringResource(R.string.register),
            color = colorResource(R.color.violetaApagado),
            onClick = {
                registerButtonPressed()
            }
        )
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    loginButtonPressed: () -> Unit,
    registerButtonPressed: () -> Unit
){
    Box(modifier = modifier.fillMaxSize()){
        PlainBackground()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            StartScreenBody(
                loginButtonPressed = loginButtonPressed,
                registerButtonPressed = registerButtonPressed
            )
        }
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Composable
@Preview
fun StartScreenPreview(){
    CompMovilProyectoTheme {
        StartScreen(
            loginButtonPressed = {},
            registerButtonPressed = {}
        )
    }
}