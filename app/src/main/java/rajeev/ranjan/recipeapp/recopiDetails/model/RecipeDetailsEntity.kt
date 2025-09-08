package rajeev.ranjan.recipeapp.recopiDetails.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe_details",
    indices = [Index(value = ["recipeId"], unique = true)]
)
data class RecipeDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "recipeId")
    val recipeId: Int,

    val title: String,
    val image: String?,
    val imageType: String?,
    val servings: Int,
    val readyInMinutes: Int,
    val cookingMinutes: Int,
    val preparationMinutes: Int,
    val license: String?,
    val sourceName: String?,
    val sourceUrl: String?,
    val spoonacularSourceUrl: String?,
    val healthScore: Double,
    val spoonacularScore: Double,
    val pricePerServing: Double,
    val cheap: Boolean,
    val creditsText: String?,
    val dairyFree: Boolean,
    val gaps: String?,
    val glutenFree: Boolean,
    val instructions: String?,
    val ketogenic: Boolean,
    val lowFodmap: Boolean,
    val sustainable: Boolean,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean,
    val veryPopular: Boolean,
    val weightWatcherSmartPoints: Int,
    val summary: String
)