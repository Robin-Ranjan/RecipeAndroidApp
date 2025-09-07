package rajeev.ranjan.recipeapp.core.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    @Serializable
    data object Home : AppRoute

    @Serializable
    data object Search : AppRoute
}