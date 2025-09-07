package rajeev.ranjan.recipeapp.core

import kotlinx.serialization.Serializable

@Serializable
data class RecipeApiResponse(
    val recipes: List<RecipeDto>
)