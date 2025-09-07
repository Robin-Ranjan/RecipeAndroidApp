package rajeev.ranjan.recipeapp.core.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import rajeev.ranjan.recipeapp.core.RecipeDao
import rajeev.ranjan.recipeapp.core.RecipeEntity
import rajeev.ranjan.recipeapp.core.RecipeIngredientDao
import rajeev.ranjan.recipeapp.core.RecipeIngredientEntity

@Database(
    entities = [RecipeEntity::class, RecipeIngredientEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): RecipeIngredientDao
}

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}