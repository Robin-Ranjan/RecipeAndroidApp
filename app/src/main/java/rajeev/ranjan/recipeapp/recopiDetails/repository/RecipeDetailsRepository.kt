package rajeev.ranjan.recipeapp.recopiDetails.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.core.RecipeIngredientDao
import rajeev.ranjan.recipeapp.favorite.roomDb.FavoriteRecipeDao
import rajeev.ranjan.recipeapp.core.apiService.RecipeApiService
import rajeev.ranjan.recipeapp.recopiDetails.helper.toDomain
import rajeev.ranjan.recipeapp.recopiDetails.helper.toDto
import rajeev.ranjan.recipeapp.recopiDetails.helper.toEntity
import rajeev.ranjan.recipeapp.recopiDetails.model.RecipeDetailsDao
import rajeev.ranjan.recipeapp.search.module.RecipeDetailUiModel

class RecipeDetailsRepository(
    private val api: RecipeApiService,
    private val recipeDetailsDao: RecipeDetailsDao,
    private val ingredientDao: RecipeIngredientDao,
    private val favoriteRecipeDao: FavoriteRecipeDao
) {

    fun recipeDetails(id: String): Flow<ResponseWrapper<RecipeDetailUiModel>> = flow {
        val recipeId = id.toInt()

        try {
            // Step 1: Ensure data exists in DB (fetch if needed)
            ensureDataExists(recipeId, id)

            // Step 2: Always observe from DB after ensuring data exists
            val detailsFlow = recipeDetailsDao.getRecipeDetails(recipeId)
            val ingredientsFlow = ingredientDao.getIngredientsForRecipe(recipeId)
            val favFlow = favoriteRecipeDao.isFavoriteFlow(id)

            // Step 3: Combine all flows and emit only when we have complete data
            combine(detailsFlow, ingredientsFlow, favFlow) { details, ingredients, isFav ->
                details?.let { recipeDetails ->
                    val uiModel = RecipeDetailUiModel(
                        recipeDetailsDto = recipeDetails.toDomain(ingredients.map { it.toDto() }),
                        isFavorite = isFav
                    )
                    ResponseWrapper.Success(uiModel)
                }
                // If details is null, return null (don't emit anything - keeps UI loading)
            }.collect { result ->
                // Only emit when result is not null (i.e., when we have actual data)
                result?.let { emit(it) }
            }

        } catch (e: Exception) {
            Log.e("RecipeRepository", "Exception in recipeDetails", e)
            emit(ResponseWrapper.Error(e))
        }
    }

    private suspend fun ensureDataExists(recipeId: Int, id: String) {
        val existingDetails = recipeDetailsDao.getRecipeDetailsOnce(recipeId)

        if (existingDetails == null) {
            Log.d("RecipeRepository", "Data not found in DB, fetching from API...")

            when (val apiResponse = api.getRecipeDetail(id)) {
                is ResponseWrapper.Success -> {
                    apiResponse.data?.let { recipe ->
                        // 1. Insert parent recipe first
                        val existingRecipe = recipeDetailsDao.getRecipeDetailsOnce(recipeId)
                        if (existingRecipe == null) {
                            recipeDetailsDao.insertRecipeDetails(recipe.toEntity())
                        }

                        // 2. Insert details
                        recipeDetailsDao.insertRecipeDetails(recipe.toEntity())

                        // 3. Delete old ingredients & insert new
                        ingredientDao.deleteIngredientsForRecipe(recipeId)
                        ingredientDao.insertIngredients(
                            recipe.recipeIngredientDtos?.map { it.toEntity(recipeId) }
                                ?: emptyList()
                        )

                        Log.d("RecipeRepository", "Data saved to DB successfully")
                    }
                }

                is ResponseWrapper.Error -> {
                    throw Exception(apiResponse.cause)
                }
            }
        } else {
            Log.d("RecipeRepository", "Data already exists in DB")
        }
    }

}