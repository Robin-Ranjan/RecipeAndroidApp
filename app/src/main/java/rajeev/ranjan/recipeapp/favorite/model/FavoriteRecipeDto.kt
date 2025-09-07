package rajeev.ranjan.recipeapp.favorite.model

data class FavoriteRecipeDto(
    val recipeId: Int,
    val title: String,
    val imageUrl: String?,
    val savedAt: Long,
    val savedAtFormatted: String,
    val isFavorite: Boolean = true
)