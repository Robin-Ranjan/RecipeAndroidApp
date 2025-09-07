package rajeev.ranjan.recipeapp.favorite.roomDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity

@Dao
interface FavoriteRecipeDao {
    @Query("SELECT * FROM favorite_recipes ORDER BY savedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT * FROM favorite_recipes WHERE recipeId = :recipeId")
    suspend fun getFavoriteById(recipeId: Int): FavoriteRecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteRecipeEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite_recipes WHERE recipeId = :recipeId")
    suspend fun deleteFavoriteById(recipeId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipeId = :recipeId)")
    suspend fun isFavorite(recipeId: Int): Boolean

    @Query("UPDATE favorite_recipes SET notificationScheduled = :scheduled WHERE recipeId = :recipeId")
    suspend fun updateNotificationScheduled(recipeId: Int, scheduled: Boolean)

    @Query("UPDATE favorite_recipes SET notificationShown = :shown WHERE recipeId = :recipeId")
    suspend fun updateNotificationShown(recipeId: Int, shown: Boolean)

    @Query("SELECT * FROM favorite_recipes WHERE notificationScheduled = 1 AND notificationShown = 0 AND notificationTime <= :currentTime")
    suspend fun getRecipesForNotification(currentTime: Long): List<FavoriteRecipeEntity>
}