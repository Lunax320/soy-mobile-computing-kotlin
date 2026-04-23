package com.example.soymusicreviewapp.ui.editprofile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.ui.utils.BackButton
import com.example.soymusicreviewapp.ui.utils.GeneralButton
import com.example.soymusicreviewapp.ui.utils.GeneralForm
import com.example.soymusicreviewapp.ui.utils.SoyBackground

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: EditProfileViewModel
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onBackClick()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SoyBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                BackButton(onBack = onBackClick)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Profile",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            GeneralForm(
                labelId = R.string.name_2,
                textValue = state.name,
                onValueChanged = { viewModel.onNameChange(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            GeneralForm(
                labelId = R.string.user,
                textValue = state.username,
                onValueChanged = { viewModel.onUsernameChange(it) }
            )

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            GeneralButton(
                text = if (state.isLoading) "Saving..." else "Save Changes",
                onClick = { viewModel.updateProfile() },
                modifier = Modifier.fillMaxWidth().height(70.dp),
                color = if (state.isLoading) Color.Gray else MaterialTheme.colorScheme.secondary
            )
        }
    }
}
