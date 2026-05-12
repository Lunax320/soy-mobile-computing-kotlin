package com.example.soymusicreviewapp.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.*

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    //modifier: Modifier = Modifier.testTag("loginScreen"),
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = state.navigate) {
        if (state.navigate) {
            navigateToHome()
        }
    }

    LaunchedEffect(key1 = state.showMessage) {
        if (state.showMessage) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }

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
                onLoginButtonPressed = { viewModel.onLoginButtonPressed() },
                onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() }
            )
        }
    }
}

@Composable
fun LoginScreenBody(
    state: LoginState,
    onUserChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginButtonPressed: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
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
            text = stringResource(R.string.email),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.email,
            textValue = state.userText,
            onValueChanged = onUserChange,
            //modifier = Modifier.testTag("txtEmailLogin")
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
            isPassword = true,
            showPassword = state.showPassword,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            //modifier = Modifier.testTag("txtPasswordLogin")
        )

        Spacer(modifier = Modifier.height(50.dp))

        GeneralButton(
            text = stringResource(R.string.login),
            //modifier = Modifier.testTag("btnLoginFinal"),
            onClick = {
                onLoginButtonPressed()
            }
        )
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
            viewModel = viewModel(),
            navigateToHome = {}
        )
    }
}
