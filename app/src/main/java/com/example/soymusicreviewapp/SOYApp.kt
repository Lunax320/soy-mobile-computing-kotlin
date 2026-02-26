package com.example.soymusicreviewapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.soymusicreviewapp.ui.navigation.AppNavigation
import com.example.soymusicreviewapp.ui.navigation.SOYBottomNavigationBar
import com.example.soymusicreviewapp.ui.navigation.Screen
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.soymusicreviewapp.ui.screens.explore.ExploreScreen
import com.example.soymusicreviewapp.ui.theme.CompMovilProyectoTheme

@Composable
fun SOYApp() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBar = currentRoute != Screen.StartScreen.route &&
            currentRoute != Screen.LoginScreen.route &&
            currentRoute != Screen.RegisterScreen.route

    Scaffold(
        bottomBar = {
            if (showBar) {
                SOYBottomNavigationBar(
                    navController = navController
                )
            }
        }
    ){
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(it)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GreetingPreview2() {
    CompMovilProyectoTheme {
        Scaffold(
            bottomBar = {

            }
        ){
            ExploreScreen(Modifier.padding(it))
        }
    }
}
