package rajeev.ranjan.recipeapp.homeScreen.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.core.RecipeApiResponse
import rajeev.ranjan.recipeapp.core.apiService.RecipeApiService

class HomeRepositoryImpl(private val api: RecipeApiService) : HomeRepository {
    override suspend fun getRandomRecipe(): Flow<ResponseWrapper<RecipeApiResponse>> = flow {
        emit(api.getRandomRecipe())
    }
}