package rajeev.ranjan.recipeapp.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.homeScreen.api.HomeApi
import rajeev.ranjan.recipeapp.homeScreen.api.sampleSearchResult
import rajeev.ranjan.recipeapp.search.module.SearchResult

class RecipeRepositoryImpl(
    private val api: HomeApi,
    private val recipeDao: RecipeDao,
    private val ingredientDao: RecipeIngredientDao
) : RecipeRepository {

    override fun getAllRecipes(): Flow<List<RecipeUiModel>> {
        return recipeDao.getAllRecipes().map { entities ->
            entities.map { entity ->
                val ingredients = ingredientDao.getIngredientsForRecipe(entity.id)
                entity.toUiModel(ingredients)
            }
        }
    }

    override fun getFavoriteRecipes(): Flow<List<RecipeUiModel>> {
        return recipeDao.getFavoriteRecipes().map { entities ->
            entities.map { entity ->
                val ingredients = ingredientDao.getIngredientsForRecipe(entity.id)
                entity.toUiModel(ingredients)
            }
        }
    }

    override suspend fun getRecipeById(recipeId: Int): RecipeUiModel? {
        val entity = recipeDao.getRecipeById(recipeId) ?: return null
        val ingredients = ingredientDao.getIngredientsForRecipe(recipeId)
        return entity.toUiModel(ingredients)
    }

    override suspend fun fetchRandomRecipes(): Result<Unit> {
        return try {
            val response = api.getRandomRecipe()
            if (response is ResponseWrapper.Success) {
                val apiResponse = response.data as RecipeApiResponse

                // Save recipes to database (avoiding duplicates with REPLACE strategy)
                val recipeEntities = apiResponse.recipes.map { it.toEntity() }
                recipeDao.insertRecipes(recipeEntities)

                // Save ingredients for each recipe
                apiResponse.recipes.forEach { recipe ->
                    val ingredientEntities = recipe.extendedIngredients.map {
                        it.toEntity(recipe.id)
                    }
                    ingredientDao.insertIngredients(ingredientEntities)
                }

                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch recipes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(recipeId: Int) {
        val recipe = recipeDao.getRecipeById(recipeId)
        recipe?.let {
            recipeDao.updateFavoriteStatus(recipeId, !it.isFavorite)
        }
    }

    override suspend fun searchRecipes(query: String): Flow<ResponseWrapper<SearchResult>> =
        flow {
            emit(ResponseWrapper.Success(sampleSearchResult))
        }
}