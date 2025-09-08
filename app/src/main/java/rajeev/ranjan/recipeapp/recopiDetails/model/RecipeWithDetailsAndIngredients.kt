package rajeev.ranjan.recipeapp.recopiDetails.model

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction

data class RecipeWithDetailsAndIngredients(
    @Embedded val details: RecipeDetailsEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeId"
    )
    val ingredients: List<RecipeIngredientEntity>
)

@Dao
interface RecipeWithDetailsDao {
    @Transaction
    @Query("SELECT * FROM recipe_details WHERE recipeId = :recipeId")
    suspend fun getRecipeWithDetailsAndIngredients(recipeId: Int): RecipeWithDetailsAndIngredients?
}
