package rajeev.ranjan.recipeapp.homeScreen.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rajeev.ranjan.recipeapp.favorite.screen.FavoriteRecipeScreenRoot

@Composable
fun Dashboard() {
    val navController = rememberNavController()
    val statusBarHeight by rememberUpdatedState(
        newValue = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    )
    val navigationBarHeight by rememberUpdatedState(
        newValue = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    )
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(statusBarHeight)
            )
        },
        bottomBar = {
            Column {
                BottomNavBar(navController = navController)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(navigationBarHeight)
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreenRoot() }
            composable(BottomNavItem.Fav.route) { FavoriteRecipeScreenRoot() }
        }
    }
}