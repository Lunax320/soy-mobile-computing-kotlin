package com.example.soymusicreviewapp.ui.screens.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.*

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            kotlinx.coroutines.delay(2000)
            onSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PlainBackground()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ForgotPasswordHeader(onBackClick = onBackClick)

            ForgotPasswordBody(
                email = state.email,
                isLoading = state.isLoading,
                onEmailChange = { viewModel.onEmailChange(it) },
                onSubmitClick = { viewModel.sendResetEmail() }
            )
        }

        if (state.showMessage) {
            LaunchedEffect(state.showMessage) {
                android.widget.Toast.makeText(context, state.message, android.widget.Toast.LENGTH_LONG).show()
                viewModel.onMessageShown()
            }
        }
    }
}

@Composable
fun ForgotPasswordHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.bg_plain_top_v2),
            contentDescription = stringResource(R.string.background_plain_top_type_2)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onBack = onBackClick)

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Restablecer Contraseña",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ForgotPasswordBody(
    email: String,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        LogoSoy(modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.8f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.email),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.Start),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        GeneralForm(
            labelId = R.string.email,
            textValue = email,
            onValueChanged = onEmailChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        GeneralButton(
            text = if (isLoading) "Enviando..." else "Enviar correo",
            onClick = onSubmitClick,
            color = if (isLoading) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Revisa tu bandeja de spam si no ves el correo en unos minutos",
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
            fontSize = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    CompMovilProyectoTheme {
        ForgotPasswordScreen(
            onBackClick = {},
            onSuccess = {},
            viewModel = viewModel()
        )
    }
}