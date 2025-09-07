package rajeev.ranjan.recipeapp.homeScreen.api

import rajeev.ranjan.networkmodule.IApiServiceClientProvider
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.networkmodule.SafeRequest.safeGet
import rajeev.ranjan.recipeapp.core.RecipeApiResponse
import rajeev.ranjan.recipeapp.search.module.SearchItem
import rajeev.ranjan.recipeapp.search.module.SearchResult

class HomeApi(private val client: IApiServiceClientProvider) {
    suspend fun getRandomRecipe(): ResponseWrapper<RecipeApiResponse> {
        return client.safeGet(
            rawPath = "recipes/random",
            queryParams = mapOf("number" to "1")
        )
    }

    suspend fun getSearchRecipe(query: String): ResponseWrapper<SearchResult> {
        return client.safeGet(
            rawPath = "recipes/complexSearch",
            queryParams = mapOf("query" to query)
        )
    }

//    suspend fun getRecipeDetail(id:String):ResponseWrapper<>
}


val sampleSearchResult = SearchResult(
    results = listOf(
        SearchItem("1", "Spaghetti Carbonara", "https://spoonacular.com/recipeImages/1-556x370.jpg", "jpg"),
        SearchItem("2", "Chicken Biryani", "https://spoonacular.com/recipeImages/2-556x370.jpg", "jpg"),
        SearchItem("3", "Paneer Butter Masala", "https://spoonacular.com/recipeImages/3-556x370.jpg", "jpg"),
        SearchItem("4", "Veggie Burger", "https://spoonacular.com/recipeImages/4-556x370.jpg", "jpg"),
        SearchItem("5", "Pancakes with Maple Syrup", "https://spoonacular.com/recipeImages/5-556x370.jpg", "jpg"),
        SearchItem("6", "Caesar Salad", "https://spoonacular.com/recipeImages/6-556x370.jpg", "jpg"),
        SearchItem("7", "Margarita Pizza", "https://spoonacular.com/recipeImages/7-556x370.jpg", "jpg"),
        SearchItem("8", "Butter Chicken", "https://spoonacular.com/recipeImages/8-556x370.jpg", "jpg"),
        SearchItem("9", "French Fries", "https://spoonacular.com/recipeImages/9-556x370.jpg", "jpg"),
        SearchItem("10", "Tandoori Chicken", "https://spoonacular.com/recipeImages/10-556x370.jpg", "jpg")
    ),
    offset = 0,
    number = 10,
    totalResults = 100
)
