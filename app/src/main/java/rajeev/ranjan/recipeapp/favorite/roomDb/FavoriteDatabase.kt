package rajeev.ranjan.recipeapp.favorite.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity
import java.util.Date

@Database(
    entities = [FavoriteRecipeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FavoriteRecipeDatabase : RoomDatabase() {

    abstract fun favoriteRecipeDao(): FavoriteRecipeDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRecipeDatabase? = null

        fun getDatabase(context: Context): FavoriteRecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteRecipeDatabase::class.java,
                    "favorite_recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Type Converters (if needed for future extensions)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}