package com.example.soymusicreviewapp.ui.screens.authentication

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme
import com.example.soymusicreviewapp.ui.utils.PlainBackground
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.GeneralForm
import com.example.soymusicreviewapp.ui.utils.LogoSoy
import com.example.soymusicreviewapp.ui.utils.TextSoy

@Composable
fun RegisterScreenBody(modifier: Modifier = Modifier) {

    var nameText by remember { mutableStateOf("") }
    var userText by remember { mutableStateOf("") }
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    Column (
        modifier = modifier.padding(horizontal = 35.dp)
    ){
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
            color = Color.White,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = (R.string.name_2),
            textValue = nameText,
            onValueChanged = { newText -> nameText = newText }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.user),
            color = Color.White,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.user,
            textValue = userText,
            onValueChanged = { newText -> userText = newText }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.email),
            color = Color.White,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.email,
            textValue = emailText,
            onValueChanged = { newText -> emailText = newText }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.password),
            color = Color.White,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )

        GeneralForm(
            labelId = R.string.password,
            textValue = passwordText,
            onValueChanged = { newText -> passwordText = newText },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(30.dp))

        GeneralButton(
            text = stringResource(R.string.create_account),
            onClick = {
                Log.d("Register screen", "Name: $nameText")
                Log.d("Register screen", "User: $userText")
                Log.d("Register screen", "Email: $emailText")
                Log.d("Register screen", "Password: $passwordText")
            }
        )
    }
}

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        PlainBackground()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            RegisterScreenBody()
        }
    }
}

//--------------------------------------------------------------------------------------------------
// PREVIEWS
//--------------------------------------------------------------------------------------------------
@Composable
@Preview
fun RegisterScreenPreview(){
    CompMovilProyectoTheme {
        RegisterScreen()
    }
}