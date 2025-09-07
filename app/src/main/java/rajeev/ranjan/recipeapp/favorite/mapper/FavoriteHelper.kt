package rajeev.ranjan.recipeapp.favorite.mapper

import android.icu.text.SimpleDateFormat
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeDto
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity
import java.util.Date
import java.util.Locale

fun FavoriteRecipeEntity.toDto(): FavoriteRecipeDto {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
    return FavoriteRecipeDto(
        recipeId = recipeId,
        title = title,
        imageUrl = imageUrl,
        savedAt = savedAt,
        savedAtFormatted = dateFormat.format(Date(savedAt)),
        isFavorite = true
    )
}

fun FavoriteRecipeDto.toEntity(
    notificationScheduled: Boolean = false,
    notificationShown: Boolean = false,
    notificationTime: Long? = null
): FavoriteRecipeEntity {
    return FavoriteRecipeEntity(
        recipeId = recipeId,
        title = title,
        imageUrl = imageUrl,
        savedAt = savedAt,
        notificationScheduled = notificationScheduled,
        notificationShown = notificationShown,
        notificationTime = notificationTime
    )
}

data class CreateFavoriteRequest(
    val recipeId: Int,
    val title: String,
    val imageUrl: String?
) {
    fun toEntity(): FavoriteRecipeEntity {
        val currentTime = System.currentTimeMillis()
        val notificationTime = currentTime + (60 * 60 * 1000) // 1 hour later

        return FavoriteRecipeEntity(
            recipeId = recipeId,
            title = title,
            imageUrl = imageUrl,
            savedAt = currentTime,
            notificationScheduled = true,
            notificationShown = false,
            notificationTime = notificationTime
        )
    }
}