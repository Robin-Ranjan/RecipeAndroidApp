package rajeev.ranjan.recipeapp.search.module

import kotlinx.serialization.Serializable

@Serializable
data class SimilarRecipes(
    val id: Int,
    val title: String,
    val imageType: String,
    val readyInMinutes: Int,
    val servings: Int,
    val image: String
)
