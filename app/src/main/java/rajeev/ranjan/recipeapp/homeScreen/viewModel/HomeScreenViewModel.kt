package rajeev.ranjan.recipeapp.homeScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rajeev.ranjan.recipeapp.core.RecipeRepository
import rajeev.ranjan.recipeapp.core.RecipeUiModel
import rajeev.ranjan.recipeapp.homeScreen.repository.HomeRepository

class HomeScreenViewModel(
    private val homeRepository: HomeRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    init {
//        fetchRandomRecipes()
        loadRecipes()
    }

    fun onAction(action: Action) {
        when (action) {
            Action.Retry -> retry()
            Action.ClearError -> clearError()
        }
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            recipeRepository.getAllRecipes().collect { recipes ->
                updateState(
                    recipes = recipes,
                    isLoading = false,
                    isEmpty = recipes.isEmpty()
                )
            }
        }
    }

    private fun fetchRandomRecipes() {
        viewModelScope.launch {
            updateState(isRefreshing = true)

            recipeRepository.fetchRandomRecipes()
                .onSuccess {
                    updateState(
                        isRefreshing = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    updateState(
                        isRefreshing = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }

    private fun updateState(
        recipes: List<RecipeUiModel> = state.value.recipes,
        isLoading: Boolean = state.value.isLoading,
        isRefreshing: Boolean = state.value.isRefreshing,
        error: String? = state.value.error,
        isEmpty: Boolean = state.value.isEmpty
    ) {
        _state.update {
            it.copy(
                recipes = recipes,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                error = error,
                isEmpty = isEmpty
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun retry() {
        fetchRandomRecipes()
    }

    data class UiState(
        val recipes: List<RecipeUiModel> = emptyList(),
        val isLoading: Boolean = true,
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val isEmpty: Boolean = false
    )

    sealed interface Action {
        data object Retry : Action
        data object ClearError : Action
    }
}