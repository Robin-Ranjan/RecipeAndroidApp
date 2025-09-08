package rajeev.ranjan.recipeapp.core.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import rajeev.ranjan.recipeapp.homeScreen.screen.Dashboard
import rajeev.ranjan.recipeapp.search.screen.SearchScreenRoot
import rajeev.ranjan.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun App(intent: Intent) {
    val navController = rememberNavController()

    RecipeAppTheme {
        NavigationProvider.navController = navController

        NavHost(
            navController = navController,
            startDestination = AppRoute.Home
        ) {
            composable<AppRoute.Home> {
                Dashboard()
            }

            composable<AppRoute.Search>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern =
                            "recipeapp://search?query={query}&recipeId={recipeId}&fromNotification={fromNotification}"
                    },
                    navDeepLink {
                        uriPattern = "recipeapp://search?query={query}"
                    },
                    navDeepLink {
                        uriPattern = "recipeapp://search"
                    }
                )
            ) { backStackEntry ->

                val args = backStackEntry.toRoute<AppRoute.Search>()
                SearchScreenRoot()
            }
        }
    }
}