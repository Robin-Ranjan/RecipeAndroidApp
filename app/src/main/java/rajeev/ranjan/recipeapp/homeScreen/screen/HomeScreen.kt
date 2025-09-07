package rajeev.ranjan.recipeapp.homeScreen.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.RecipeUiModel
import rajeev.ranjan.recipeapp.core.navigation.AppRoute
import rajeev.ranjan.recipeapp.core.navigation.NavigationProvider
import rajeev.ranjan.recipeapp.homeScreen.screen.component.RecipeItemCard
import rajeev.ranjan.recipeapp.homeScreen.viewModel.HomeScreenViewModel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

@Composable
fun HomeScreenRoot(viewModel: HomeScreenViewModel = koinViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    HomeScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun HomeScreen(
    uiState: HomeScreenViewModel.UiState,
    onAction: (HomeScreenViewModel.Action) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val lazyListState = rememberLazyListState()

    val searchBarIndex = 1
    val isSearchBarVisible = remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex >= searchBarIndex
        }
    }

    Scaffold(
        containerColor = AppColor.WHITE,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
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
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppColor.ORANGE
                )
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Gap(height = 16.dp)
                        // 👋 Hey <user first name>
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "👋",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "Hey Rajeev",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Subtitle
                        Text(
                            text = "Discover tasty and healthy recipe",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        )
                    }

                    item {
                        // Search Bar
                        FakeSearchBar(
                            onClick = { NavigationProvider.navController.navigate(AppRoute.Search) },
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    item {
                        PopularRecipesSection(
                            item = uiState.recipes.take(10)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    item {
                        Text(
                            text = "All Recipes",
                            modifier = Modifier,
                            style = AppTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W700,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = AppColor.PRIMARY_BLACK
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(uiState.recipes, key = { it.id }) {
                        RecipeItemCard(item = it)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                AnimatedVisibility(
                    visible = isSearchBarVisible.value,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300)),
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(300)),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(AppColor.WHITE)
                ) {
                    Column {
                        FakeSearchBar(onClick = { }, modifier = Modifier.padding(16.dp))
                        // Add a subtle divider for better visual separation
                        HorizontalDivider(
                            color = AppColor.GREY_2,
                            thickness = 1.dp,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopularRecipesSection(
    item: List<RecipeUiModel>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text(
            text = "Popular Recipes",
            style = AppTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W700,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = AppColor.PRIMARY_BLACK
            ),
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            maxLines = 1,
            maxItemsInEachRow = 10
        ) {
            item.forEach {
                RecipeCard(it)
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: RecipeUiModel) {
    Box(
        modifier = Modifier
            .width(156.dp)
            .height(156.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray)
    ) {
        AsyncImage(
            model = recipe.image,
            contentDescription = "sds",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 200f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp)
        ) {
            Text(
                text = recipe.title,
                style = AppTheme.typography.bodySmall.copy(
                    color = AppColor.WHITE,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ready in ${recipe.readyInMinutes} min",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun FakeSearchBar(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = Color(0xFFF5F6FA),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.search_icon),
                contentDescription = "Search",
                tint = AppColor.PRIMARY_BLACK
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Search Any Recipe",
                style = MaterialTheme.typography.bodyMedium.copy(color = AppColor.SECONDARY)
            )
        }
    }
    Gap(height = 16.dp)
}
