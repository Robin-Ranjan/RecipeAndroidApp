package rajeev.ranjan.recipeapp.core

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String?,
    val imageType: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val sourceUrl: String?,
    val vegetarian: Boolean = false,
    val vegan: Boolean = false,
    val glutenFree: Boolean = false,
    val dairyFree: Boolean = false,
    val veryHealthy: Boolean = false,
    val cheap: Boolean = false,
    val veryPopular: Boolean = false,
    val sustainable: Boolean = false,
    val lowFodmap: Boolean = false,
    val weightWatcherSmartPoints: Int?,
    val gaps: String?,
    val preparationMinutes: Int?,
    val cookingMinutes: Int?,
    val aggregateLikes: Int = 0,
    val healthScore: Double?,
    val creditsText: String?,
    val license: String?,
    val sourceName: String?,
    val pricePerServing: Double?,
    val extendedIngredients: List<IngredientDto> = emptyList()
)

@Serializable
data class IngredientDto(
    val id: Int,
    val aisle: String?,
    val image: String?,
    val consistency: String?,
    val name: String,
    val nameClean: String?,
    val original: String,
    val originalName: String?,
    val amount: Double,
    val unit: String,
    val meta: List<String> = emptyList()
)
