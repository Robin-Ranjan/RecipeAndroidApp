package rajeev.ranjan.recipeapp.search.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.base_ui.AppBottomSheet
import rajeev.ranjan.recipeapp.core.base_ui.DragHandleType
import rajeev.ranjan.recipeapp.core.utils.orDefault
import rajeev.ranjan.recipeapp.search.screen.component.RecipeBottomSheet
import rajeev.ranjan.recipeapp.search.screen.component.AppSearchBar
import rajeev.ranjan.recipeapp.search.viewModel.BottomSheetType
import rajeev.ranjan.recipeapp.search.viewModel.SearchViewmodel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

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

//    var notificationSheet by remember { mutableStateOf(false) }
//    var showFavoriteSnackbar by remember { mutableStateOf(false) }

//    LaunchedEffect(showFavoriteSnackbar) {
//        if (showFavoriteSnackbar) {
//            val result = snackbarHostState.showSnackbar(
//                message = "Added to Favorites",
//                actionLabel = "Add Reminder",
//                duration = SnackbarDuration.Short
//            )
//            if (result == SnackbarResult.ActionPerformed) {
//                notificationSheet = true
//            }
//            showFavoriteSnackbar = false
//        }
//    }


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
                    ) {
                        items(uiState.data) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 10.dp)
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
                                Spacer(modifier = Modifier.width(8.dp))
                                HighlightedText(
                                    fullText = it.title,
                                    query = uiState.query,
                                    normalStyle = AppTheme.typography.bodySmall.copy(color = AppColor.PRIMARY_BLACK),
                                    highlightStyle = AppTheme.typography.bodySmall.copy(
                                        color = AppColor.PRIMARY_BLACK,
                                        fontWeight = FontWeight.Bold
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
                            BottomSheetType.INGREDIENT_DETAILS
                        )
                    )
                },
                onDismiss = { onAction(SearchViewmodel.Action.ShowBottomSheet(null)) },
                onFavClick = {
                    onAction(SearchViewmodel.Action.OnFavClick(uiState.selectedRecipe!!))
                }
            )

        }

        if (uiState.activeBottomSheet == BottomSheetType.INGREDIENT_DETAILS) {
            uiState.selectedRecipe?.let { data ->
                IngredientBottomSheet(
                    title = data.recipeDetailsDto.title.orDefault(),
                    image = data.recipeDetailsDto.image.orDefault(),
                    readyInMinutes = data.recipeDetailsDto.readyInMinutes.toString(),
                    servings = data.recipeDetailsDto.servings.toString(),
                    onGetIngredients = {},
                    onDismiss = { onAction(SearchViewmodel.Action.ShowBottomSheet(BottomSheetType.RECIPE_DETAILS)) }
                )
            }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientBottomSheet(
    title: String,
    image: String,
    readyInMinutes: String,
    servings: String,
    onDismiss: () -> Unit,
    onGetIngredients: () -> Unit
) {
    AppBottomSheet(
        onDismiss = onDismiss,
        type = DragHandleType.BACK
    ) { state, scope ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Recipe Title and Heart Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AppColor.PRIMARY_BLACK
                    ),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(R.drawable.fav_icon),
                    contentDescription = "Add to favorites",
                    tint = AppColor.GREY_2,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* Handle favorite click */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recipe Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Ready in
                Column {
                    Text(
                        text = "Ready in",
                        style = AppTheme.typography.bodySmall.copy(
                            color = AppColor.GREY_2
                        )
                    )
                    Text(
                        text = "$readyInMinutes min",
                        style = AppTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AppColor.ORANGE
                        )
                    )
                }

                // Servings
                Column {
                    Text(
                        text = "Servings",
                        style = AppTheme.typography.bodySmall.copy(
                            color = AppColor.GREY_2
                        )
                    )
                    Text(
                        text = servings,
                        style = AppTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AppColor.PRIMARY_BLACK
                        )
                    )
                }

                // Price per serving
                Column {
                    Text(
                        text = "Price/serving",
                        style = AppTheme.typography.bodySmall.copy(
                            color = AppColor.GREY_2
                        )
                    )
                    Text(
                        text = "₹${5}",
                        style = AppTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AppColor.PRIMARY_BLACK
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Get Ingredients Button
            Button(
                onClick = onGetIngredients,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColor.ORANGE
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Get Ingredients",
                    style = AppTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = AppColor.WHITE
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AppColor.WHITE,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
