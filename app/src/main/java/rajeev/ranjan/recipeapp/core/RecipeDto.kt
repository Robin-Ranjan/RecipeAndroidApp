package rajeev.ranjan.recipeapp.core

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String?,
    val veryPopular: Boolean,
    val readyInMinutes: Int?,
)
