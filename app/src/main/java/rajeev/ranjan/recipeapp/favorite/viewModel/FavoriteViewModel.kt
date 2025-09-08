package rajeev.ranjan.recipeapp.favorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity
import rajeev.ranjan.recipeapp.favorite.repository.FavoriteRecipeRepository

class FavoriteViewModel(private val favoriteRecipeRepository: FavoriteRecipeRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    init {
        fetchFavoriteRecipes()
    }

    fun onAction(action: Action) {
        when (action) {
            Action.ResetError -> _state.update { it.copy(error = null) }
        }
    }

    private fun fetchFavoriteRecipes() {
        viewModelScope.launch {
            if (_state.value.favoriteRecipes.isEmpty()) {
                _state.update { it.copy(isLoading = true) }
            }
            favoriteRecipeRepository.getAllFavoritesFlow().collect { favoritesList ->
                _state.update { it.copy(favoriteRecipes = favoritesList, isLoading = false) }
            }
        }
    }


    data class UiState(
        val favoriteRecipes: List<FavoriteRecipeEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Action {
        data object ResetError : Action
    }
}