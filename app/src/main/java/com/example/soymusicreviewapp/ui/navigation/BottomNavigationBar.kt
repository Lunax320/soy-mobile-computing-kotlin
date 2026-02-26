package com.example.soymusicreviewapp.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.soymusicreviewapp.R

data class BottomNavItem(
    val filledIcon: ImageVector,
    val outlineIcon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(Icons.Filled.Home, Icons.Outlined.Home, Screen.ForYouFeedScreen.route),
    BottomNavItem(Icons.Filled.Search, Icons.Outlined.Search, Screen.ExploreScreen.route),
    BottomNavItem(Icons.Filled.AddCircle, Icons.Outlined.AddCircle, Screen.CreateReviewScreen.route),
    BottomNavItem(Icons.Filled.Notifications, Icons.Outlined.Notifications, Screen.NotificationScreen.route),
    BottomNavItem(Icons.Filled.Person, Icons.Outlined.Person, Screen.ProfileScreen.route),
)

@Composable
fun SOYBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box {
        Image(
            painter = painterResource(R.drawable.bg_plain_bottom),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )

        NavigationBar(
            containerColor = Color.Transparent,
            modifier = modifier,
        ) {

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(item.route)
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.filledIcon else item.outlineIcon,
                            contentDescription = item.route,
                            tint = if (isSelected) colorResource(R.color.violetaClaro) else colorResource(R.color.violetaIcono)
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun SOYBottomNavigationBarPreview() {
    SOYBottomNavigationBar(
        navController = rememberNavController()
    )
}
