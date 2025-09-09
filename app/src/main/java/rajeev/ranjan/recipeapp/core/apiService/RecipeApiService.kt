package rajeev.ranjan.recipeapp.core.apiService

import rajeev.ranjan.networkmodule.IApiServiceClientProvider
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.networkmodule.SafeRequest.safeGet
import rajeev.ranjan.recipeapp.core.RecipeApiResponse
import rajeev.ranjan.recipeapp.search.module.RecipeDetailsDto
import rajeev.ranjan.recipeapp.search.module.SearchResult
import rajeev.ranjan.recipeapp.search.module.SimilarRecipes

class RecipeApiService(private val client: IApiServiceClientProvider) {
    suspend fun getRandomRecipe(): ResponseWrapper<RecipeApiResponse> {
        return client.safeGet(
            rawPath = "recipes/random",
            queryParams = mapOf("number" to "40")
        )
    }

    suspend fun getSearchRecipe(query: String): ResponseWrapper<SearchResult> {
        return client.safeGet(
            rawPath = "recipes/complexSearch",
            queryParams = mapOf("query" to query)
        )
    }

    suspend fun getRecipeDetail(id: String): ResponseWrapper<RecipeDetailsDto> {
        return client.safeGet(
            rawPath = "recipes/$id/information",
        )
    }

    suspend fun getSimilarRecipe(id: String): ResponseWrapper<List<SimilarRecipes>> {
        return client.safeGet(
            rawPath = "recipes/$id/similar",
        )
    }
}
