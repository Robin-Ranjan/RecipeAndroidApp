package rajeev.ranjan.recipeapp.favorite.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "recipe_id")
    val recipeId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "ready_in_minutes")
    val readyInMinutes: String?,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "notification_time")
    val notificationTime: Long = System.currentTimeMillis() + (60 * 60 * 1000),

    @ColumnInfo(name = "is_notified")
    val isNotified: Boolean = false
)