package com.example.soymusicreviewapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.BackButton
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.SettingsOption
import com.example.soymusicreviewapp.ui.utils.SoyBackground


@Composable
fun SettingsScreenHeader(
        modifier: Modifier = Modifier,
        onBackClick: () -> Unit = {}
    ){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackButton(
                        modifier = Modifier
                            .padding(10.dp),
                        onBack = onBackClick
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = stringResource(R.string.configuration),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }

@Composable
fun SettingsScreenBody(
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        SoyBackground()

        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize()
        ) {
            SettingsOption(
                title = stringResource(R.string.sign_out),
                subtitle = stringResource(R.string.log_out_of_your_account),
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                containerColor = MaterialTheme.colorScheme.background,
                onClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsOption(
                title = stringResource(R.string.delete_account),
                subtitle = stringResource(R.string.delete_your_account_permantly),
                icon = Icons.Filled.Delete,
                containerColor = MaterialTheme.colorScheme.error,
                onClick = onDeleteAccountClick
            )
        }
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onConfirmLogout: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SettingsScreenHeader(
            onBackClick = onBackClick
        )
        SettingsScreenBody(
            onLogoutClick = { viewModel.onLogoutClicked() },
            onDeleteAccountClick = { viewModel.onDeleteAccountClicked() }
        )
    }

    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLogoutDialog() },
            title = { Text(text = stringResource(R.string.sign_out)) },
            text = { Text(text = "¿Estás seguro de que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.confirmLogout()
                    onConfirmLogout() 
                }) {
                    Text(text = "Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissLogoutDialog() }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    if (state.showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteAccountDialog() },
            title = { Text(text = stringResource(R.string.delete_account)) },
            text = { Text(text = "¿Estás seguro de que quieres eliminar tu cuenta permanentemente? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.confirmDeleteAccount() },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteAccountDialog() }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}
//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------

@Composable
@Preview
fun SettingsScreenHeaderPreview() {
    CompMovilProyectoTheme {
        SettingsScreenHeader()
    }
}


@Composable
@Preview
fun SettingsScreenPreview(){
    CompMovilProyectoTheme {
        SettingsScreen()
    }
}

@Composable
@Preview
fun SettingScreenBodyPreview() {
    CompMovilProyectoTheme {
        SettingsScreenBody(
            onLogoutClick = {},
            onDeleteAccountClick = {}
        )
    }
}

@Composable
@Preview
fun GeneralButtonPreview(){
    CompMovilProyectoTheme {
        GeneralButton(
            text = "Login",
            fontSize = 20.sp
        )
    }
}
