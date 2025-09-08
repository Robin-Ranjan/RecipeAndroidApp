package rajeev.ranjan.recipeapp.search.module

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailUiModel(
    val recipeDetailsDto: RecipeDetailsDto,
    val isFavorite: Boolean
)
