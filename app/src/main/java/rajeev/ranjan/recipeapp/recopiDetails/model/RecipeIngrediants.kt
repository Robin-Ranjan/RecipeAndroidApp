package rajeev.ranjan.recipeapp.recopiDetails.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "recipe_ingredients",
    indices = [
        Index(value = ["recipeId"]),
        Index(value = ["recipeId", "ingredientId"], unique = true)
    ]
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
)


@Serializable
data class RecipeIngredientDto(
    val aisle: String? = null,
    val amount: Double,
    val consistency: String? = null,
    val id: Int,
    val image: String? = null,
    val name: String,
    val original: String,
    val originalName: String,
    val unit: String? = null
)