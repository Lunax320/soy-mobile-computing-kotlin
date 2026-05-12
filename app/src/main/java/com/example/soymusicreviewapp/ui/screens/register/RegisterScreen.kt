package com.example.soymusicreviewapp.ui.screens.register

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
fun RegisterScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier.testTag("registerScreen"),
    viewModel: RegisterViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = state.navigate) {
        if (state.navigate) {
            navigateToHome()
        }
    }

    Box(modifier = modifier) {
        PlainBackground()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            RegisterScreenBody(
                state = state,
                onNameChange = { viewModel.onNameChange(it) },
                onUserChange = { viewModel.onUserChange(it) },
                onEmailChange = { viewModel.onEmailChange(it) },
                onPasswordChange = { viewModel.onPasswordChange(it) },
                onRegisterButtonPressed = { viewModel.onRegisterButtonPressed() },
                onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
                onMessageShown = { viewModel.onMessageShown() }
            )
        }
    }
}

@Composable
fun RegisterScreenBody(
    state: RegisterState,
    onNameChange: (String) -> Unit,
    onUserChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterButtonPressed: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onMessageShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 35.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LogoSoy(modifier = Modifier.size(200.dp))
            Spacer(modifier = Modifier.width(14.dp))
            TextSoy(size = 50.sp, modifier = Modifier.padding(top = 75.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.full_name),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.name_2,
            textValue = state.nameText,
            onValueChanged = onNameChange,
            modifier = Modifier.testTag("txtfullname")
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.user),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.user,
            textValue = state.userText,
            onValueChanged = onUserChange,
            modifier = Modifier.testTag("txtuser")
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.email),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.email,
            textValue = state.emailText,
            onValueChanged = onEmailChange,
            modifier = Modifier.testTag("txtemail")
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
            modifier = Modifier.testTag("txtpassword")
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (state.showMessage) {
            Text(
                text = state.errorMessage,
                color = androidx.compose.ui.graphics.Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("errorMessage"),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        GeneralButton(
            text = stringResource(R.string.create_account),
            //modifier = Modifier.testTag("btnRegisterFinal"),
            onClick = {
                onRegisterButtonPressed()
            }
        )
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Composable
@Preview
fun RegisterScreenPreview(){
    CompMovilProyectoTheme {
        RegisterScreen(
            viewModel = viewModel(),
            navigateToHome = {}
        )
    }
}
