package rajeev.ranjan.recipeapp.favorite.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey
    val recipeId: Int,
    val title: String,
    val imageUrl: String?,
    val savedAt: Long,
    val notificationScheduled: Boolean = false,
    val notificationShown: Boolean = false,
    val notificationTime: Long? = null
)