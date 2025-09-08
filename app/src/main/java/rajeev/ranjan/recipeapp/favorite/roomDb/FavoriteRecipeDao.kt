package rajeev.ranjan.recipeapp.favorite.roomDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity

@Dao
interface FavoriteRecipeDao {

    @Query("SELECT * FROM favorite_recipes ORDER BY created_at DESC")
    fun getAllFavoritesFlow(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipe_id = :recipeId)")
    fun isFavoriteFlow(recipeId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipe_id = :recipeId)")
    fun isFavorite(recipeId: String): Boolean


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteRecipeEntity): Long

    @Query("DELETE FROM favorite_recipes WHERE recipe_id = :recipeId")
    suspend fun deleteFavoriteByRecipeId(recipeId: String)


    @Query("UPDATE favorite_recipes SET notification_time = :notificationTime WHERE recipe_id = :recipeId")
    suspend fun updateNotificationTime(recipeId: String, notificationTime: Long)

    @Query(
        """
        SELECT * FROM favorite_recipes 
        WHERE notification_time <= :currentTime 
        AND is_notified = 0
        ORDER BY notification_time ASC
    """
    )
    suspend fun getPendingNotifications(currentTime: Long): List<FavoriteRecipeEntity>


    @Query("UPDATE favorite_recipes SET is_notified = 1 WHERE recipe_id = :recipeId")
    suspend fun markAsNotified(recipeId: String)
}