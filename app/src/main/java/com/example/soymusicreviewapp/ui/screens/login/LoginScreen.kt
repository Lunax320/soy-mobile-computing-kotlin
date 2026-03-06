package com.example.soymusicreviewapp.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.GeneralForm
import com.example.soymusicreviewapp.ui.utils.LogoSoy
import com.example.soymusicreviewapp.ui.utils.TextSoy

@Composable
fun LoginScreenBody(
    state: LoginState,
    onUserChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    loginButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 35.dp)
    ){
        Spacer(modifier = Modifier.height(140.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LogoSoy(modifier = Modifier.size(200.dp))

            Spacer(modifier = Modifier.width(14.dp))

            TextSoy(size = 50.sp, modifier = Modifier.padding(top = 75.dp))
        }
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = stringResource(R.string.user),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.user,
            textValue = state.userText,
            onValueChanged = onUserChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.password),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.password,
            textValue = state.passwordText,
            onValueChanged = onPasswordChange,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(50.dp))
        GeneralButton(
            text = stringResource(R.string.login),
            onClick = {
                loginButtonPressed()
            }
        )
    }
}

@Composable
fun LoginScreen(
    loginButtonPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = modifier) {
        PlainBackground()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            LoginScreenBody(
                state = state,
                onUserChange = { viewModel.onUserChange(it) },
                onPasswordChange = { viewModel.onPasswordChange(it) },
                loginButtonPressed = loginButtonPressed
            )
        }
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Composable
@Preview
fun LoginScreenPreview(){
    CompMovilProyectoTheme {
        LoginScreen(
            loginButtonPressed = {}
        )
    }
}
