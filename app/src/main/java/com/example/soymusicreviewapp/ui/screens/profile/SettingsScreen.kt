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
import com.example.soymusicreviewapp.ui.utils.CloseButton
import com.example.soymusicreviewapp.ui.utils.DeleteButton
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.PlainBackground


@Composable
fun SettingsScreenHeader(
        modifier: Modifier = Modifier
    ){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(colorResource(R.color.violetaClaro))
                .padding(horizontal = 24.dp, vertical = 20.dp)
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
                    Spacer(modifier = Modifier.width(50.dp))
                    Text(
                        text = stringResource(R.string.configuracion),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }

@Composable
fun SettingsScreenBody(
    onClosesClick: () -> Unit,
    onDeletesClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        PlainBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                GeneralButton(
                    text = stringResource(R.string.cerrar_sesion),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    color = colorResource(R.color.magentaOscuro)
                )

                CloseButton(
                    onClose = onClosesClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {

                GeneralButton(
                    text = stringResource(R.string.eliminar_cuenta),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    color = colorResource(R.color.Burdeos)
                )

                DeleteButton(
                    onDelete = onDeletesClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
            }
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
            modifier = Modifier.fillMaxSize(),
            onClosesClick = {},
            onDeletesClick = {}
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
        onClosesClick = {},
        onDeletesClick = {},
        modifier = Modifier
    )
}


