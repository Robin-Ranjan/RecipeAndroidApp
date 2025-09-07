package rajeev.ranjan.recipeapp.homeScreen.repository

import kotlinx.coroutines.flow.Flow
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.core.RecipeApiResponse

interface HomeRepository {
    suspend fun getRandomRecipe(): Flow<ResponseWrapper<RecipeApiResponse>>
}