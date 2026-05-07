package com.example.soymusicreviewapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.soymusicreviewapp.ui.navigation.AppNavigation
import com.example.soymusicreviewapp.ui.navigation.SOYBottomNavigationBar
import com.example.soymusicreviewapp.ui.navigation.Screen
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.contracts.contract

@Composable
fun SOYApp() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if(task.isSuccessful) {
            Log.d("Token",task.result)
    } else{
            Log.d("Token","Error al obtener el token")
        }
}
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    val showBar = currentRoute != Screen.StartScreen.route &&
            currentRoute != Screen.LoginScreen.route &&
            currentRoute != Screen.RegisterScreen.route &&
            currentRoute != Screen.SplashScreen.route &&
            currentRoute?.startsWith("commentReview") != true

    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(

        contract= ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if(isGranted) {
                Log.d("Notification", "Permiso concedido")
            }else{
                Log.d("Notification", "Permiso denegado")
            }
        }
    )


    /*LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }*/



    Scaffold(
        bottomBar = {
            if (showBar) {
                SOYBottomNavigationBar(
                    navController = navController
                )
            }
        }
    ) { paddingValues ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}