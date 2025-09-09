package rajeev.ranjan.recipeapp.fullDetails.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rajeev.ranjan.recipeapp.core.RecipeRepository
import rajeev.ranjan.recipeapp.core.navigation.AppRoute
import rajeev.ranjan.recipeapp.core.utils.orDefault
import rajeev.ranjan.recipeapp.favorite.repository.FavoriteRecipeRepository
import rajeev.ranjan.recipeapp.recopiDetails.repository.RecipeDetailsRepository
import rajeev.ranjan.recipeapp.search.module.RecipeDetailUiModel

class RecipeDetailsViewModel(
    private val recipeDetailsRepository: RecipeDetailsRepository,
    private val favRepository: FavoriteRecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id = savedStateHandle.toRoute<AppRoute.RecipeDetails>().id

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState()
        )

    init {
        fetchRecipeDetails(id)
    }

    fun onAction(action: Action) {
        when (action) {
            Action.Retry -> fetchRecipeDetails(id)
            Action.FavClick -> toggleFavorite()
            is Action.UpdateNotificationTime -> updateNotificationTime(action.time)
        }
    }

    private fun fetchRecipeDetails(id: String) {
        viewModelScope.launch {
            recipeDetailsRepository.recipeDetails(id).collect { result ->
                result.onSuccess {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        recipeDetailUiModel = it
                    )
                }
                result.onFailure {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = it.message
                    )
                }
            }
        }
    }

    private fun updateNotificationTime(time: Long) {
        viewModelScope.launch {
            favRepository.updateNotificationTime(
                recipeId = _state.value.recipeDetailUiModel?.recipeDetailsDto?.id.orDefault()
                    .toString(),
                notificationTime = time
            )
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            val isFav = _state.value.recipeDetailUiModel?.isFavorite.orDefault()
            if (isFav) {
                favRepository.removeFromFavorites(
                    _state.value.recipeDetailUiModel?.recipeDetailsDto?.id.orDefault().toString()
                )
            } else {
                favRepository.addToFavorites(
                    recipeId = _state.value.recipeDetailUiModel?.recipeDetailsDto?.id.orDefault()
                        .toString(),
                    imageUrl = _state.value.recipeDetailUiModel?.recipeDetailsDto?.image.orDefault(),
                    title = _state.value.recipeDetailUiModel?.recipeDetailsDto?.title.orDefault(),
                    readyInMinutes = _state.value.recipeDetailUiModel?.recipeDetailsDto?.readyInMinutes.orDefault()
                        .toString()
                )
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val recipeDetailUiModel: RecipeDetailUiModel? = null,
        val error: String? = null
    )

    sealed interface Action {
        data object Retry : Action
        data object FavClick : Action
        data class UpdateNotificationTime(val time: Long) : Action
    }
}