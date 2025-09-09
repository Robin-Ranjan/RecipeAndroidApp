package rajeev.ranjan.recipeapp.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.core.apiService.RecipeApiService
import rajeev.ranjan.recipeapp.search.module.SearchResult
import rajeev.ranjan.recipeapp.search.module.SimilarRecipes

class RecipeRepositoryImpl(
    private val api: RecipeApiService,
    private val recipeDao: RecipeDao,
) : RecipeRepository {

    override fun getAllRecipes(): Flow<List<RecipeUiModel>> {
        return recipeDao.getAllRecipes().map { entities ->
            entities.map { entity ->
                entity.toUiModel()
            }
        }
    }

    override suspend fun fetchRandomRecipes(): Result<Unit> {
        return try {
            val response = api.getRandomRecipe()
            if (response is ResponseWrapper.Success) {
                val apiResponse = response.data as RecipeApiResponse

                val recipeEntities = apiResponse.recipes.map { it.toEntity() }
                recipeDao.insertRecipes(recipeEntities)

                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch recipes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchRecipes(query: String): Flow<ResponseWrapper<SearchResult>> =
        flow {
            emit(api.getSearchRecipe(query))
        }

    override suspend fun getSimilarRecipes(id: String): Flow<ResponseWrapper<List<SimilarRecipes>>> =
        flow {
            emit(api.getSimilarRecipe(id))
        }
}