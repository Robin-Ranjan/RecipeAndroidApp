package rajeev.ranjan.recipeapp.core

import kotlinx.coroutines.flow.Flow
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.search.module.SearchResult
import rajeev.ranjan.recipeapp.search.module.SimilarRecipes

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<RecipeUiModel>>
    suspend fun fetchRandomRecipes(): Result<Unit>

    suspend fun searchRecipes(query: String): Flow<ResponseWrapper<SearchResult>>

    suspend fun getSimilarRecipes(id: String): Flow<ResponseWrapper<List<SimilarRecipes>>>
}