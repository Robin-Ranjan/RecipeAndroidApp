package rajeev.ranjan.recipeapp.core

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int?,
    val veryPopular: Boolean,
    val fetchedAt: Long = System.currentTimeMillis(),
)