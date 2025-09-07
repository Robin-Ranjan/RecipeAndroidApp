package rajeev.ranjan.recipeapp.core

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val image: String?,
    val imageType: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val sourceUrl: String?,
    val vegetarian: Boolean = false,
    val vegan: Boolean = false,
    val glutenFree: Boolean = false,
    val dairyFree: Boolean = false,
    val veryHealthy: Boolean = false,
    val cheap: Boolean = false,
    val veryPopular: Boolean = false,
    val sustainable: Boolean = false,
    val lowFodmap: Boolean = false,
    val weightWatcherSmartPoints: Int?,
    val gaps: String?,
    val preparationMinutes: Int?,
    val cookingMinutes: Int?,
    val aggregateLikes: Int = 0,
    val healthScore: Double?,
    val creditsText: String?,
    val license: String?,
    val sourceName: String?,
    val pricePerServing: Double?,
    val fetchedAt: Long = System.currentTimeMillis(), // Track when fetched
    val isFavorite: Boolean = false // Local favorite status
)

@Entity(
    tableName = "recipe_ingredients",
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["recipeId"])]
)
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipeId: Int,
    val ingredientId: Int,
    val aisle: String?,
    val image: String?,
    val consistency: String?,
    val name: String,
    val nameClean: String?,
    val original: String,
    val originalName: String?,
    val amount: Double,
    val unit: String,
    val meta: String // JSON string of meta array
)