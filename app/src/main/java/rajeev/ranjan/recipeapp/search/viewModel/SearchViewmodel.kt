package rajeev.ranjan.recipeapp.search.viewModel

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
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
import rajeev.ranjan.recipeapp.core.RecipeUiModel
import rajeev.ranjan.recipeapp.core.toEntity
import rajeev.ranjan.recipeapp.core.toUiModel
import rajeev.ranjan.recipeapp.search.module.SearchItem
import rajeev.ranjan.recipeapp.search.module.SearchResult

@OptIn(FlowPreview::class)
class SearchViewmodel(private val repository: RecipeRepository) : ViewModel() {

    private val _state = MutableStateFlow(UState())
    val state = _state.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UState()
        )

    private var searchJob: Job? = null

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
            is Action.OnQueryChange -> {
                _state.value = _state.value.copy(query = action.query)
            }
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

    data class UState(
        val data: List<SearchItem> = emptyList(),
        val isSearching: Boolean = false,
        val query: String = "",
        val error: String = ""
    )

    sealed interface Action {
        data class OnQueryChange(val query: String) : Action
    }
}