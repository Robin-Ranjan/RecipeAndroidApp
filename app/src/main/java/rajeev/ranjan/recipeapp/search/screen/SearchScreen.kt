package rajeev.ranjan.recipeapp.search.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.search.screen.component.AppSearchBar
import rajeev.ranjan.recipeapp.search.screen.component.RecipeBottomSheet
import rajeev.ranjan.recipeapp.search.screen.component.RecipeInformationSheet
import rajeev.ranjan.recipeapp.search.screen.component.SimilarRecipesBottomSheet
import rajeev.ranjan.recipeapp.search.viewModel.BottomSheetType
import rajeev.ranjan.recipeapp.search.viewModel.SearchViewmodel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap
import rajeev.ranjan.recipeapp.ui.theme.UpdateStatusBarAppearance

@Composable
fun SearchScreenRoot(viewModel: SearchViewmodel = koinViewModel()) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            if (it.isNotEmpty()) {
                snackbarHostState.showSnackbar(it)
                viewModel.onAction(SearchViewmodel.Action.ResetMessage)
            }
        }
    }

    SearchScreen(
        uiState,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
fun SearchScreen(
    uiState: SearchViewmodel.UState,
    snackbarHostState: SnackbarHostState,
    onAction: (SearchViewmodel.Action) -> Unit,
) {
    val statusBarHeight by rememberUpdatedState(
        newValue = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    )
    UpdateStatusBarAppearance(AppColor.WHITE)

    Scaffold(
        containerColor = AppColor.WHITE,
        snackbarHost = {
            SnackbarHost(snackbarHostState, modifier = Modifier.padding(bottom = 16.dp))
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Rtl),
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            {
                AppSearchBar(
                    query = uiState.query,
                    onAction = onAction,
                    statusBarHeight = statusBarHeight
                )

                Gap(height = 8.dp)

                if (uiState.isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.data) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 10.dp)
                                    .clickable {
                                        onAction(
                                            SearchViewmodel.Action.SelectRecipe(
                                                it.id
                                            )
                                        )
                                    }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.recipe_icon),
                                    contentDescription = "Recipe",
                                    tint = AppColor.SECONDARY,
                                    modifier = Modifier.size(20.dp)
                                )
                                Gap(width = 8.dp)
                                HighlightedText(
                                    fullText = it.title,
                                    query = uiState.query,
                                    normalStyle = AppTheme.typography.bodySmall.copy(
                                        color = AppColor.PRIMARY_BLACK,
                                        fontWeight = FontWeight.W400,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    ),
                                    highlightStyle = AppTheme.typography.bodySmall.copy(
                                        color = AppColor.PRIMARY_BLACK,
                                        fontWeight = FontWeight.W700,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

        }
        if (uiState.activeBottomSheet == BottomSheetType.RECIPE_DETAILS) {
            RecipeBottomSheet(
                data = uiState.selectedRecipe,
                onGetIngredients = {
                    onAction(
                        SearchViewmodel.Action.ShowBottomSheet(
                            BottomSheetType.RECIPE_INSTRUCTIONS
                        )
                    )
                },
                onDismiss = { onAction(SearchViewmodel.Action.ShowBottomSheet(null)) },
                onFavClick = {
                    onAction(SearchViewmodel.Action.OnFavClick(uiState.selectedRecipe!!))
                }
            )

        }

        if (uiState.activeBottomSheet == BottomSheetType.RECIPE_INSTRUCTIONS) {
            uiState.selectedRecipe?.let { data ->
                RecipeInformationSheet(
                    recipeModel = data,
                    onGetSimilarRecipe = {
                        onAction(
                            SearchViewmodel.Action.ShowBottomSheet(
                                BottomSheetType.SIMILAR_RECOPIES,
                                id = data.recipeDetailsDto.id.toString()
                            )
                        )
                    },
                    onFavClick = { onAction(SearchViewmodel.Action.OnFavClick(data)) },
                    onDismiss = { onAction(SearchViewmodel.Action.ShowBottomSheet(BottomSheetType.RECIPE_DETAILS)) }
                )
            }
        }

        if (uiState.activeBottomSheet == BottomSheetType.SIMILAR_RECOPIES) {
            SimilarRecipesBottomSheet(
                recipesName = uiState.selectedRecipe?.recipeDetailsDto?.title ?: "",
                isFavorite = uiState.selectedRecipe?.isFavorite ?: false,
                isSimilarLoading = uiState.similarLoading,
                data = uiState.similarRecipes,
                onFavClick = { onAction(SearchViewmodel.Action.OnFavClick(uiState.selectedRecipe!!)) },
                onDismiss = { onAction(SearchViewmodel.Action.ShowBottomSheet(BottomSheetType.RECIPE_INSTRUCTIONS)) }
            )
        }
    }
}

@Composable
fun HighlightedText(
    fullText: String,
    query: String,
    normalStyle: TextStyle,
    highlightStyle: TextStyle
) {
    val annotatedString = buildAnnotatedString {
        if (query.isEmpty()) {
            append(fullText)
        } else {
            val regex = Regex(query, RegexOption.IGNORE_CASE)
            var lastIndex = 0
            regex.findAll(fullText).forEach { matchResult ->
                val start = matchResult.range.first
                val end = matchResult.range.last + 1

                // Append text before match (normal)
                append(fullText.substring(lastIndex, start))

                // Append match with highlight
                withStyle(
                    style = SpanStyle(
                        fontWeight = highlightStyle.fontWeight,
                        color = highlightStyle.color
                    )
                ) {
                    append(fullText.substring(start, end))
                }

                lastIndex = end
            }
            // Append remaining text
            if (lastIndex < fullText.length) {
                append(fullText.substring(lastIndex))
            }
        }
    }

    Text(
        text = annotatedString,
        style = normalStyle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

