package com.example.soymusicreviewapp.ui.screens.profile

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.utils.BackButton
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.SettingsOption
import com.example.soymusicreviewapp.ui.utils.SoyBackground


@Composable
fun SettingsScreenHeader(
        modifier: Modifier = Modifier
    ){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(colorResource(R.color.violetaApagado))
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
                        onBack = {}
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = stringResource(R.string.configuration),
                        color = Color.White,
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
                title = stringResource(R.string.cerrar_sesion),
                subtitle = "Salir de tu cuenta",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                containerColor = colorResource(R.color.azul2),
                onClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsOption(
                title = stringResource(R.string.eliminar_cuenta),
                subtitle = "Eliminar permanentemente tu cuenta",
                icon = Icons.Filled.Delete,
                containerColor = colorResource(R.color.rojo),
                onClick = onDeleteAccountClick
            )
        }
    }
}
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SettingsScreenHeader()
        SettingsScreenBody(
            onLogoutClick = {},
            onDeleteAccountClick = {}
        )
    }
}
//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------

@Composable
@Preview
fun SettingsScreenHeaderPreview() {
    SettingsScreenHeader()
}


@Composable
@Preview
fun SettingsScreenPreview(){
    SettingsScreen()
}

@Composable
@Preview
fun SettingScreenBodyPreview() {
    SettingsScreenBody(
        onLogoutClick = {},
        onDeleteAccountClick = {}
    )
}

@Composable
@Preview
fun GeneralButtonPreview(){
    GeneralButton(
        text = "Login",
        fontSize = 20.sp
    )
}

