package rajeev.ranjan.recipeapp.core

import kotlinx.coroutines.flow.Flow
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.search.module.SearchResult

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<RecipeUiModel>>
    fun getFavoriteRecipes(): Flow<List<RecipeUiModel>>
    suspend fun getRecipeById(recipeId: Int): RecipeUiModel?
    suspend fun fetchRandomRecipes(): Result<Unit>
    suspend fun toggleFavorite(recipeId: Int)

    suspend fun searchRecipes(query: String): Flow<ResponseWrapper<SearchResult>>
}