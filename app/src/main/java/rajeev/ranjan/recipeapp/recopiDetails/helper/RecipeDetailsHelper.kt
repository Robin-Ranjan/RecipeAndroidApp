package rajeev.ranjan.recipeapp.recopiDetails.helper

import rajeev.ranjan.recipeapp.core.utils.orDefault
import rajeev.ranjan.recipeapp.recopiDetails.model.RecipeIngredientDto
import rajeev.ranjan.recipeapp.recopiDetails.model.RecipeIngredientEntity
import rajeev.ranjan.recipeapp.recopiDetails.model.RecipeDetailsEntity
import rajeev.ranjan.recipeapp.search.module.RecipeDetailsDto

fun RecipeDetailsDto.toEntity(): RecipeDetailsEntity {
    return RecipeDetailsEntity(
        recipeId = this.id.orDefault(),
        title = this.title.orDefault(),
        image = this.image,
        imageType = this.imageType,
        servings = this.servings.orDefault(),
        readyInMinutes = this.readyInMinutes.orDefault(),
        cookingMinutes = this.cookingMinutes ?: 5,
        preparationMinutes = this.preparationMinutes ?: 5,
        license = this.license,
        sourceName = this.sourceName,
        sourceUrl = this.sourceUrl,
        spoonacularSourceUrl = this.spoonacularSourceUrl,
        healthScore = this.healthScore.orDefault(),
        spoonacularScore = this.spoonacularScore.orDefault(),
        pricePerServing = this.pricePerServing.orDefault(),
        cheap = this.cheap.orDefault(),
        creditsText = this.creditsText,
        dairyFree = this.dairyFree.orDefault(),
        gaps = this.gaps,
        glutenFree = this.glutenFree.orDefault(),
        instructions = this.instructions,
        ketogenic = this.ketogenic.orDefault(),
        lowFodmap = this.lowFodmap.orDefault(),
        sustainable = this.sustainable.orDefault(),
        vegan = this.vegan.orDefault(),
        vegetarian = this.vegetarian.orDefault(),
        veryHealthy = this.veryHealthy.orDefault(),
        veryPopular = this.veryPopular.orDefault(),
        weightWatcherSmartPoints = this.weightWatcherSmartPoints.orDefault(),
        summary = this.summary.orDefault()
    )
}

// Entity -> Domain (for reading from Room)
// Note: Extended ingredients must be loaded separately from RecipeIngredientDao
fun RecipeDetailsEntity.toDomain(
    ingredients: List<RecipeIngredientDto> = emptyList()
): RecipeDetailsDto {
    return RecipeDetailsDto(
        id = this.recipeId,
        title = this.title,
        image = this.image ?: "",
        imageType = this.imageType ?: "",
        servings = this.servings,
        readyInMinutes = this.readyInMinutes,
        cookingMinutes = this.cookingMinutes,
        preparationMinutes = this.preparationMinutes,
        license = this.license,
        sourceName = this.sourceName,
        sourceUrl = this.sourceUrl,
        spoonacularSourceUrl = this.spoonacularSourceUrl ?: "",
        healthScore = this.healthScore,
        spoonacularScore = this.spoonacularScore,
        pricePerServing = this.pricePerServing,
        cheap = this.cheap,
        creditsText = this.creditsText,
        cuisines = emptyList(), // not stored in DB
        dairyFree = this.dairyFree,
        diets = emptyList(), // not stored in DB
        gaps = this.gaps,
        glutenFree = this.glutenFree,
        instructions = this.instructions,
        ketogenic = this.ketogenic,
        lowFodmap = this.lowFodmap,
        occasions = emptyList(), // not stored in DB
        sustainable = this.sustainable,
        vegan = this.vegan,
        vegetarian = this.vegetarian,
        veryHealthy = this.veryHealthy,
        veryPopular = this.veryPopular,
        weightWatcherSmartPoints = this.weightWatcherSmartPoints,
        dishTypes = emptyList(), // not stored in DB
        recipeIngredientDtos = ingredients,
        summary = this.summary
    )
}

fun RecipeIngredientEntity.toDto(): RecipeIngredientDto {
    return RecipeIngredientDto(
        aisle = aisle,
        amount = amount,
        consistency = consistency,
        id = ingredientId,
        image = image,
        name = name,
        original = original,
        originalName = originalName ?: "",
        unit = unit
    )
}

fun RecipeIngredientDto.toEntity(recipeId: Int): RecipeIngredientEntity {
    return RecipeIngredientEntity(
        recipeId = recipeId,
        ingredientId = id,
        aisle = aisle,
        image = image,
        consistency = consistency,
        name = name,
        nameClean = null, // not available in DTO
        original = original,
        originalName = originalName,
        amount = amount,
        unit = unit ?: "",
    )
}
