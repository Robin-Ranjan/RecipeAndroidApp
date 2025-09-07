package rajeev.ranjan.recipeapp.core

data class RecipeUiModel(
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val aggregateLikes: Int,
    val healthScore: Double?,
    val isFavorite: Boolean,
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val ingredients: List<IngredientUiModel> = emptyList()
)

data class IngredientUiModel(
    val name: String,
    val original: String,
    val amount: Double,
    val unit: String,
    val image: String?
)

// Conversion Extensions
fun RecipeDto.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        image = image,
        imageType = imageType,
        readyInMinutes = readyInMinutes,
        servings = servings,
        sourceUrl = sourceUrl,
        vegetarian = vegetarian,
        vegan = vegan,
        glutenFree = glutenFree,
        dairyFree = dairyFree,
        veryHealthy = veryHealthy,
        cheap = cheap,
        veryPopular = veryPopular,
        sustainable = sustainable,
        lowFodmap = lowFodmap,
        weightWatcherSmartPoints = weightWatcherSmartPoints,
        gaps = gaps,
        preparationMinutes = preparationMinutes,
        cookingMinutes = cookingMinutes,
        aggregateLikes = aggregateLikes,
        healthScore = healthScore,
        creditsText = creditsText,
        license = license,
        sourceName = sourceName,
        pricePerServing = pricePerServing
    )
}

fun IngredientDto.toEntity(recipeId: Int): RecipeIngredientEntity {
    return RecipeIngredientEntity(
        recipeId = recipeId,
        ingredientId = id,
        aisle = aisle,
        image = image,
        consistency = consistency,
        name = name,
        nameClean = nameClean,
        original = original,
        originalName = originalName,
        amount = amount,
        unit = unit,
        meta = meta.joinToString(",") // Convert list to string
    )
}

fun RecipeEntity.toUiModel(ingredients: List<RecipeIngredientEntity> = emptyList()): RecipeUiModel {
    return RecipeUiModel(
        id = id,
        title = title,
        image = image,
        readyInMinutes = readyInMinutes,
        servings = servings,
        aggregateLikes = aggregateLikes,
        healthScore = healthScore,
        isFavorite = isFavorite,
        vegetarian = vegetarian,
        vegan = vegan,
        glutenFree = glutenFree,
        dairyFree = dairyFree,
        ingredients = ingredients.map { it.toUiModel() }
    )
}

fun RecipeIngredientEntity.toUiModel(): IngredientUiModel {
    return IngredientUiModel(
        name = name,
        original = original,
        amount = amount,
        unit = unit,
        image = image?.let { "https://img.spoonacular.com/ingredients_100x100/$it" }
    )
}