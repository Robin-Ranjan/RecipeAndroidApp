package rajeev.ranjan.recipeapp.favorite.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rajeev.ranjan.recipeapp.favorite.mapper.CreateFavoriteRequest
import rajeev.ranjan.recipeapp.favorite.mapper.toDto
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeDto
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity
import rajeev.ranjan.recipeapp.favorite.roomDb.FavoriteRecipeDao

class FavoriteRecipeRepositoryImpl(
    private val dao: FavoriteRecipeDao
) : FavoriteRecipeRepository {

    override fun getAllFavorites(): Flow<List<FavoriteRecipeDto>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDto() }
        }
    }

    override suspend fun addToFavorites(request: CreateFavoriteRequest) {
        val entity = request.toEntity()
        dao.insertFavorite(entity)
        // Schedule notification here
        scheduleNotification(entity)
    }

    override suspend fun removeFromFavorites(recipeId: Int) {
        dao.deleteFavoriteById(recipeId)
        // Cancel scheduled notification here
        cancelNotification(recipeId)
    }

    override suspend fun isFavorite(recipeId: Int): Boolean {
        return dao.isFavorite(recipeId)
    }

    override suspend fun getRecipesForNotification(): List<FavoriteRecipeEntity> {
        return dao.getRecipesForNotification(System.currentTimeMillis())
    }

    override suspend fun markNotificationShown(recipeId: Int) {
        dao.updateNotificationShown(recipeId, true)
    }

    private fun scheduleNotification(recipe: FavoriteRecipeEntity) {
        // Implementation for scheduling notification
        // This would use WorkManager or AlarmManager
    }

    private fun cancelNotification(recipeId: Int) {
        // Implementation for canceling scheduled notification
    }
}