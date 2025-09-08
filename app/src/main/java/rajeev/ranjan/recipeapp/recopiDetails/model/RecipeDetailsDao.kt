package rajeev.ranjan.recipeapp.recopiDetails.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDetailsDao {
    @Query("SELECT * FROM recipe_details WHERE recipeId = :recipeId LIMIT 1")
    fun getRecipeDetails(recipeId: Int): Flow<RecipeDetailsEntity?>

    @Query("SELECT * FROM recipe_details WHERE recipeId = :recipeId LIMIT 1")
    suspend fun getRecipeDetailsOnce(recipeId: Int): RecipeDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeDetails(recipe: RecipeDetailsEntity)
}

