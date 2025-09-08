package rajeev.ranjan.recipeapp.favorite.repository

import kotlinx.coroutines.flow.Flow
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity
import rajeev.ranjan.recipeapp.favorite.roomDb.FavoriteRecipeDao

class FavoriteRecipeRepository(
    private val favoriteRecipeDao: FavoriteRecipeDao
) {

    fun getAllFavoritesFlow(): Flow<List<FavoriteRecipeEntity>> {
        return favoriteRecipeDao.getAllFavoritesFlow()
    }

    suspend fun addToFavorites(
        recipeId: String,
        customNotificationTime: Long? = null,
        imageUrl: String,
        title: String,
        readyInMinutes: String
    ): Long {
        val notificationTime = customNotificationTime
            ?: (System.currentTimeMillis() + (60 * 60 * 1000))

        val favorite = FavoriteRecipeEntity(
            recipeId = recipeId,
            imageUrl = imageUrl,
            title = title,
            readyInMinutes = readyInMinutes,
            notificationTime = notificationTime
        )
        return favoriteRecipeDao.insertFavorite(favorite)
    }

    suspend fun removeFromFavorites(recipeId: String) {
        favoriteRecipeDao.deleteFavoriteByRecipeId(recipeId)
    }

    suspend fun updateNotificationTime(recipeId: String, notificationTime: Long) {
        favoriteRecipeDao.updateNotificationTime(recipeId, notificationTime)
    }

    suspend fun getPendingNotifications(): List<FavoriteRecipeEntity> {
        val currentTime = System.currentTimeMillis()
        return favoriteRecipeDao.getPendingNotifications(currentTime)
    }

    suspend fun markAsNotified(recipeId: String) {
        favoriteRecipeDao.markAsNotified(recipeId)
    }
}