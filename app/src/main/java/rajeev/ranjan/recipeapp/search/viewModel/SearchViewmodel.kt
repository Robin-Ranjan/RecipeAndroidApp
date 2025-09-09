package rajeev.ranjan.recipeapp.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rajeev.ranjan.recipeapp.core.RecipeRepository
import rajeev.ranjan.recipeapp.favorite.repository.FavoriteRecipeRepository
import rajeev.ranjan.recipeapp.recopiDetails.repository.RecipeDetailsRepository
import rajeev.ranjan.recipeapp.search.module.RecipeDetailUiModel
import rajeev.ranjan.recipeapp.search.module.SearchItem
import rajeev.ranjan.recipeapp.search.module.SimilarRecipes

@OptIn(FlowPreview::class)
class SearchViewmodel(
    private val repository: RecipeRepository,
    private val recipeDetailsRepository: RecipeDetailsRepository,
    private val favoriteRecipeRepository: FavoriteRecipeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UState())
    val state = _state.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UState()
        )

    private var searchJob: Job? = null
    private var detailJob: Job? = null

    init {
        viewModelScope.launch {
            _state
                .map { it.query }
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        searchJob?.cancel()
                        searchRecipes(query)
                    } else {
                        _state.value = _state.value.copy(
                            data = emptyList(),
                            isSearching = false,
                            error = ""
                        )
                    }
                }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.OnQueryChange -> _state.update { it.copy(query = action.query) }

            Action.ResetMessage -> _state.update { it.copy(error = null) }
            is Action.SelectRecipe -> fetchRecipeDetails(action.recipeId)

            is Action.ShowBottomSheet -> {
                _state.update { it.copy(activeBottomSheet = action.sheet) }
                if (action.sheet == BottomSheetType.SIMILAR_RECOPIES) {
                    getSimilarRecipes(action.id!!)
                }
            }

            is Action.OnFavClick -> toggleFavorite(action.recipe)
        }
    }

    private fun searchRecipes(query: String) {
        _state.update { it.copy(isSearching = true) }
        searchJob = viewModelScope.launch {
            repository.searchRecipes(query).collect { response ->
                response.onSuccess { data ->
                    _state.value = _state.value.copy(
                        data = data.results,
                        isSearching = false
                    )
                }.onFailure {
                    _state.value = _state.value.copy(
                        error = it.message ?: "Unknown error",
                        isSearching = false
                    )
                }
            }
        }
    }

    private fun fetchRecipeDetails(recipeId: String) {
        detailJob?.cancel()
        _state.update { it.copy(isLoadingDetails = true) }
        detailJob = viewModelScope.launch {
            recipeDetailsRepository.recipeDetails(recipeId).collect { response ->
                response.onSuccess { details ->
                    _state.update {
                        it.copy(
                            selectedRecipe = details,
                            isLoadingDetails = false,
                            activeBottomSheet = BottomSheetType.RECIPE_DETAILS
                        )
                    }
                }.onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoadingDetails = false) }
                }
            }
        }
    }

    private fun toggleFavorite(recipe: RecipeDetailUiModel) {
        viewModelScope.launch {
            if (recipe.isFavorite) {
                favoriteRecipeRepository.removeFromFavorites(recipe.recipeDetailsDto.id.toString())
            } else {
                favoriteRecipeRepository.addToFavorites(
                    customNotificationTime = 10 * 60 * 1000, // keeping 10 Sec just to test
                    recipeId = recipe.recipeDetailsDto.id.toString(),
                    title = recipe.recipeDetailsDto.title ?: "",
                    imageUrl = recipe.recipeDetailsDto.image ?: "",
                    readyInMinutes = recipe.recipeDetailsDto.readyInMinutes.toString()
                )
            }
        }
    }

    private fun getSimilarRecipes(id: String) {
        _state.update { it.copy(similarLoading = true) }
        viewModelScope.launch {
            repository.getSimilarRecipes(id).collect { response ->
                response.onSuccess { data ->
                    _state.update { it.copy(similarLoading = false, similarRecipes = data) }
                }
                response.onFailure { error ->
                    _state.update { it.copy(error = error.message, similarLoading = false) }
                }
            }
        }
    }

    data class UState(
        val data: List<SearchItem> = emptyList(),
        val isSearching: Boolean = false,
        val query: String = "",
        val error: String? = null,

        val selectedRecipe: RecipeDetailUiModel? = null,
        val isLoadingDetails: Boolean = false,
        val activeBottomSheet: BottomSheetType? = null,

        val similarRecipes: List<SimilarRecipes> = emptyList(),
        val similarLoading: Boolean = false
    )

    sealed interface Action {
        data class OnQueryChange(val query: String) : Action
        data object ResetMessage : Action
        data class SelectRecipe(val recipeId: String) : Action
        data class ShowBottomSheet(val sheet: BottomSheetType?, val id: String? = null) : Action
        data class OnFavClick(val recipe: RecipeDetailUiModel) : Action
    }
}

enum class BottomSheetType {
    RECIPE_DETAILS,
    RECIPE_INSTRUCTIONS,
    SIMILAR_RECOPIES
}