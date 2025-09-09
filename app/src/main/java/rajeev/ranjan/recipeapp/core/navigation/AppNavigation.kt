package rajeev.ranjan.recipeapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import rajeev.ranjan.recipeapp.SplashScreen
import rajeev.ranjan.recipeapp.core.utils.enterTransition
import rajeev.ranjan.recipeapp.core.utils.exitTransition
import rajeev.ranjan.recipeapp.core.utils.popEnterTransition
import rajeev.ranjan.recipeapp.core.utils.popExitTransition
import rajeev.ranjan.recipeapp.fullDetails.screen.RecipeDetailsScreenRoot
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
            startDestination = AppRoute.Splash
        ) {

            composable<AppRoute.Splash>(
                enterTransition = enterTransition(),
                exitTransition = exitTransition(),
                popEnterTransition = popEnterTransition(),
                popExitTransition = popExitTransition()
            ) {
                SplashScreen {
                    navController.navigate(AppRoute.Home) {
                        popUpTo<AppRoute.Splash>() {
                            inclusive = true
                        }
                    }
                }
            }

            composable<AppRoute.Home>(
                enterTransition = enterTransition(),
                exitTransition = exitTransition(),
                popEnterTransition = popEnterTransition(),
                popExitTransition = popExitTransition()
            ) {
                Dashboard()
            }

            composable<AppRoute.Search>(
                enterTransition = enterTransition(),
                exitTransition = exitTransition(),
                popEnterTransition = popEnterTransition(),
                popExitTransition = popExitTransition()
            ) {
                SearchScreenRoot()
            }

            composable<AppRoute.RecipeDetails>(
                enterTransition = enterTransition(),
                exitTransition = exitTransition(),
                popEnterTransition = popEnterTransition(),
                popExitTransition = popExitTransition(),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "recipeapp://recipe/{id}"
                    }
                )
            ) {
                RecipeDetailsScreenRoot()
            }
        }
    }
}