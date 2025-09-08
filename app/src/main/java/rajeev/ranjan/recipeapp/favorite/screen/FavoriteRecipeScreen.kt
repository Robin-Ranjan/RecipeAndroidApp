package rajeev.ranjan.recipeapp.favorite.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import rajeev.ranjan.recipeapp.core.base_ui.ErrorScreen
import rajeev.ranjan.recipeapp.favorite.viewModel.FavoriteViewModel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

@Composable
fun FavoriteRecipeScreenRoot(viewmodel: FavoriteViewModel = koinViewModel()) {
    val uiState by viewmodel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            if (it.isNotEmpty()) {
                snackbarHostState.showSnackbar(it)
                viewmodel.onAction(FavoriteViewModel.Action.ResetError)
            }
        }
    }
    FavoriteRecipeScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = viewmodel::onAction
    )
}

@Composable
fun FavoriteRecipeScreen(
    uiState: FavoriteViewModel.UiState,
    snackbarHostState: SnackbarHostState,
    onAction: (FavoriteViewModel.Action) -> Unit
) {
    Scaffold(
        containerColor = AppColor.WHITE,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .navigationBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Favorite Recipe Screen",
                    modifier = Modifier,
                    style = AppTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W700,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = AppColor.PRIMARY_BLACK
                    )
                )

                Gap(height = 30.dp)

                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    uiState.favoriteRecipes.isEmpty() && !uiState.isLoading -> {
                        ErrorScreen(error = "No Favorite Recipes", onRetry = {})
                    }

                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.favoriteRecipes) { recipe ->
                                FavoriteItemCard(recipe)
                            }
                        }
                    }
                }
            }
        }
    }
}