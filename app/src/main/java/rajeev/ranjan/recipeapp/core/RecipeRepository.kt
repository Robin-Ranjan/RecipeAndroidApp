package rajeev.ranjan.recipeapp.core

import kotlinx.coroutines.flow.Flow
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.search.module.SearchResult

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<RecipeUiModel>>
    suspend fun fetchRandomRecipes(): Result<Unit>

    suspend fun searchRecipes(query: String): Flow<ResponseWrapper<SearchResult>>
}