package rajeev.ranjan.recipeapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rajeev.ranjan.recipeapp.homeScreen.screen.Dashboard
import rajeev.ranjan.recipeapp.search.screen.SearchScreenRoot
import rajeev.ranjan.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun App() {
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

            composable<AppRoute.Search> {
                SearchScreenRoot()
            }
        }
    }
}