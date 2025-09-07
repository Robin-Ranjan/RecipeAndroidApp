package rajeev.ranjan.recipeapp.favorite.repository

import kotlinx.coroutines.flow.Flow
import rajeev.ranjan.recipeapp.favorite.mapper.CreateFavoriteRequest
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeDto
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity

interface FavoriteRecipeRepository {
    fun getAllFavorites(): Flow<List<FavoriteRecipeDto>>
    suspend fun addToFavorites(request: CreateFavoriteRequest)
    suspend fun removeFromFavorites(recipeId: Int)
    suspend fun isFavorite(recipeId: Int): Boolean
    suspend fun getRecipesForNotification(): List<FavoriteRecipeEntity>
    suspend fun markNotificationShown(recipeId: Int)
}