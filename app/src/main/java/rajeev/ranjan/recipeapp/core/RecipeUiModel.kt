package rajeev.ranjan.recipeapp.core

import rajeev.ranjan.recipeapp.core.utils.orDefault
import rajeev.ranjan.recipeapp.recopiDetails.model.RecipeDetailsEntity
import rajeev.ranjan.recipeapp.search.module.RecipeDetailsDto

data class RecipeUiModel(
    val id: Int,
    val title: String,
    val image: String?,
    val veryPopular: Boolean,
    val readyInMinutes: Int?,
)

fun RecipeEntity.toUiModel(): RecipeUiModel {
    return RecipeUiModel(
        id = id,
        title = title,
        image = image,
        veryPopular = veryPopular,
        readyInMinutes = readyInMinutes
    )
}

fun RecipeDto.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        image = image,
        veryPopular = veryPopular,
        readyInMinutes = readyInMinutes
    )
}

// Conversion Extensions
fun RecipeDetailsDto.toEntity(): RecipeDetailsEntity {
    return RecipeDetailsEntity(
        title = title.orDefault(),
        image = image,
        imageType = imageType,
        readyInMinutes = readyInMinutes?: 0,
        servings = servings?: 0,
        sourceUrl = sourceUrl,
        vegetarian = vegetarian.orDefault(),
        vegan = vegan.orDefault(),
        glutenFree = glutenFree.orDefault(),
        dairyFree = dairyFree.orDefault(),
        veryHealthy = veryHealthy.orDefault(),
        cheap = cheap.orDefault(),
        veryPopular = veryPopular.orDefault(),
        sustainable = sustainable.orDefault(),
        lowFodmap = lowFodmap.orDefault(),
        weightWatcherSmartPoints = weightWatcherSmartPoints.orDefault(),
        gaps = gaps,
        preparationMinutes = preparationMinutes ?: 5,
        cookingMinutes = cookingMinutes ?: 5,
        healthScore = healthScore.orDefault(),
        creditsText = creditsText,
        license = license,
        sourceName = sourceName,
        pricePerServing = pricePerServing.orDefault(),
        recipeId = id.orDefault(),
        spoonacularSourceUrl = spoonacularSourceUrl,
        summary = summary.orDefault(),
        instructions = instructions,
        spoonacularScore = spoonacularScore.orDefault(),
        ketogenic = ketogenic.orDefault()
    )
}