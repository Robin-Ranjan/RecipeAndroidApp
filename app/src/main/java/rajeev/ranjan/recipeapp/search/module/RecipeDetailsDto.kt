package rajeev.ranjan.recipeapp.search.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rajeev.ranjan.recipeapp.recopiDetails.model.RecipeIngredientDto

@Serializable
data class RecipeDetailsDto(
    val id: Int? = null,
    val title: String? = null,
    val image: String? = null,
    val imageType: String? = null,
    val servings: Int? = null,
    val readyInMinutes: Int? = null,

    @SerialName("preparationMinutes") val preparationMinutes: Int? = null,
    @SerialName("cookingMinutes") val cookingMinutes: Int? = null,

    val license: String? = null,
    val sourceName: String? = null,
    val sourceUrl: String? = null,
    val spoonacularSourceUrl: String? = null,

    val healthScore: Double? = null,
    val spoonacularScore: Double? = null,

    val pricePerServing: Double? = null,
    val cheap: Boolean? = null,
    val creditsText: String? = null,

    val cuisines: List<String>? = null,
    val dairyFree: Boolean? = null,
    val diets: List<String>? = null,
    val gaps: String? = null,
    val glutenFree: Boolean? = null,
    val instructions: String? = null,
    val ketogenic: Boolean? = null,
    val lowFodmap: Boolean? = null,
    val occasions: List<String>? = null,
    val sustainable: Boolean? = null,
    val vegan: Boolean? = null,
    val vegetarian: Boolean? = null,
    val veryHealthy: Boolean? = null,
    val veryPopular: Boolean? = null,

    val weightWatcherSmartPoints: Int? = null,
    val dishTypes: List<String>? = null,

    @SerialName("extendedIngredients") val recipeIngredientDtos: List<RecipeIngredientDto>? = null,
    val summary: String? = null,
)


object FakeApi {
    private val json = Json { ignoreUnknownKeys = true }

    fun getFakeRecipe(): RecipeDetailsDto {
        val jsonString = """
            { 
              "id": 716429,
              "title": "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs",
              "image": "https://img.spoonacular.com/recipes/716429-556x370.jpg",
              "imageType": "jpg",
              "servings": 2,
              "readyInMinutes": 45,
              "cookingMinutes": 25,
              "preparationMinutes": 20,
              "license": "CC BY-SA 3.0",
              "sourceName": "Full Belly Sisters",
              "sourceUrl": "http://fullbellysisters.blogspot.com/2012/06/pasta-with-garlic-scallions-cauliflower.html",
              "spoonacularSourceUrl": "https://spoonacular.com/pasta-with-garlic-scallions-cauliflower-breadcrumbs-716429",
              "healthScore": 19.0,
              "spoonacularScore": 83.0,
              "pricePerServing": 163.15,
              "analyzedInstructions": [],
              "cheap": false,
              "creditsText": "Full Belly Sisters",
              "cuisines": [],
              "dairyFree": false,
              "diets": [],
              "gaps": "no",
              "glutenFree": false,
              "instructions": "",
              "ketogenic": false,
              "lowFodmap": false,
              "occasions": [],
              "sustainable": false,
              "vegan": false,
              "vegetarian": false,
              "veryHealthy": false,
              "veryPopular": false,
              "whole30": false,
              "weightWatcherSmartPoints": 17,
              "dishTypes": ["lunch","main course","main dish","dinner"],
              "extendedIngredients": [],
              "summary": "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs might be a good recipe...",
              "winePairing": null
            }
        """.trimIndent()

        return json.decodeFromString(RecipeDetailsDto.serializer(), jsonString)
    }
}
