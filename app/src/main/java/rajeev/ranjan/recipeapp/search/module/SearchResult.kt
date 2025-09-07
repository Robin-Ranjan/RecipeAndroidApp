package rajeev.ranjan.recipeapp.search.module

import kotlinx.serialization.Serializable

@Serializable
data class SearchItem(
    val id: String,
    val title: String,
    val image: String,
    val imageType: String
)

@Serializable
data class SearchResult(
    val results: List<SearchItem>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)