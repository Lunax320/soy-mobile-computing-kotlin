package com.example.soymusicreviewapp.ui.screens.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 90.sp, fontFamily = FontFamily.Cursive)
        )

        Spacer(modifier = Modifier.height(6.dp))

        LogoSoy()

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.live_a_life_you_will_remember),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 22.sp, fontStyle = FontStyle.Italic)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.avicii_the_nights),
            color = MaterialTheme.colorScheme.secondary,
            style = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        GeneralButton(
            text = stringResource(R.string.login),
            onClick = loginButtonPressed
        )

        Spacer(modifier = Modifier.height(4.dp))

        GeneralButton(
            text = stringResource(R.string.register),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            onClick = registerButtonPressed
        )
    }
}

@Composable
fun StartScreen(
    loginButtonPressed: () -> Unit,
    registerButtonPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StartViewModel = viewModel()
){
    val state by viewModel.uiState.collectAsState()

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
