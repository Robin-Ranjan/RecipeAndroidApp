package rajeev.ranjan.recipeapp.core.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    @Serializable
    data object Home : AppRoute

    @Serializable
    data class Search(
        val query: String = "",
        val recipeId: Int = -1,
        val fromNotification: Boolean = false
    ) : AppRoute
}