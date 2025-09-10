package rajeev.ranjan.recipeapp.fullDetails.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.androidx.compose.koinViewModel
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.base_ui.ReminderBottomSheet
import rajeev.ranjan.recipeapp.core.navigation.NavigationProvider
import rajeev.ranjan.recipeapp.core.utils.asName
import rajeev.ranjan.recipeapp.core.utils.orDefault
import rajeev.ranjan.recipeapp.core.utils.parseHtmlToText
import rajeev.ranjan.recipeapp.fullDetails.viewModel.RecipeDetailsViewModel
import rajeev.ranjan.recipeapp.search.screen.component.ExpandableRow
import rajeev.ranjan.recipeapp.search.screen.component.RecipeDetailsCard
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap
import rajeev.ranjan.recipeapp.ui.theme.UpdateStatusBarAppearance

@Composable
fun RecipeDetailsScreenRoot(viewModel: RecipeDetailsViewModel = koinViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    RecipeDetailsScreen(
        uiState,
        snackbarHostState,
        viewModel::onAction
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeDetailsScreen(
    uiState: RecipeDetailsViewModel.UiState,
    snackbarHostState: SnackbarHostState,
    onAction: (RecipeDetailsViewModel.Action) -> Unit
) {
    UpdateStatusBarAppearance(AppColor.WHITE)
    val listState = rememberScrollState()
    val imageHeight = 392.dp
    val statusBarHeight by rememberUpdatedState(
        newValue = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    )

    // Calculate the threshold outside of derivedStateOf
    val density = LocalDensity.current
    val scrollThreshold = remember { with(density) { (imageHeight - 350.dp).toPx() } }

    // Calculate if we should show the collapsed toolbar
    val isCollapsed = remember {
        derivedStateOf {
            listState.value > scrollThreshold
        }
    }
    var showAddToFavSnackbar by remember { mutableStateOf(false) }
    var showReminderAddedSnackbar by remember { mutableStateOf(false) }
    var showReminderBottomSheet by remember { mutableStateOf(false) }
    var previousFavoriteState by remember {
        mutableStateOf(
            uiState.recipeDetailUiModel?.isFavorite ?: false
        )
    }

    LaunchedEffect(uiState.recipeDetailUiModel?.isFavorite) {
        val currentFavoriteState = uiState.recipeDetailUiModel?.isFavorite ?: false

        // Show snackbar only if recipe was not previously favorited and now it is
        if (!previousFavoriteState && currentFavoriteState && showAddToFavSnackbar) {
            val result = snackbarHostState.showSnackbar(
                message = "Added to favorites",
                actionLabel = "Add Reminder",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                // Call your bottom sheet function here
                showReminderBottomSheet = true
                showAddToFavSnackbar = false
            } else {
                showAddToFavSnackbar = false
            }
        }
        previousFavoriteState = currentFavoriteState
    }

    LaunchedEffect(showReminderAddedSnackbar) {
        if (showReminderAddedSnackbar) {
            val result = snackbarHostState.showSnackbar(
                message = "Reminder added successfully",
                actionLabel = "OK",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                showReminderAddedSnackbar = false
            }
        }
    }

    Scaffold(
        containerColor = AppColor.WHITE,
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.background(AppColor.PRIMARY_BLACK)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Rtl),
                    end = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding() + 15.dp
                )
                .navigationBarsPadding()
        ) {

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppColor.ORANGE
                )
            } else {
                uiState.recipeDetailUiModel?.recipeDetailsDto?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = statusBarHeight)
                            .verticalScroll(listState)
                    ) {
                        Box(modifier = Modifier.size(imageHeight)) {
                            AsyncImage(
                                modifier = Modifier.matchParentSize(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.recipeDetailUiModel.recipeDetailsDto.image)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.recipe_icon),
                                error = painterResource(R.drawable.recipe_icon),
                                fallback = painterResource(R.drawable.recipe_icon)
                            )

                            // Title overlay on image
                            Text(
                                text = uiState.recipeDetailUiModel.recipeDetailsDto.title.orDefault(),
                                modifier = Modifier
                                    .padding(horizontal = 24.dp, vertical = 26.dp)
                                    .align(Alignment.BottomStart),
                                style = AppTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.W700,
                                    fontSize = 20.sp,
                                    lineHeight = 26.sp,
                                    color = AppColor.WHITE
                                )
                            )

                            androidx.compose.animation.AnimatedVisibility(
                                visible = !isCollapsed.value,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .align(Alignment.TopCenter)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { NavigationProvider.navController.popBackStack() },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                width = 1.dp,
                                                color = AppColor.NEUTRAL_LIGHT,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .background(AppColor.WHITE)
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.back_icon),
                                            contentDescription = "Back",
                                            tint = AppColor.PRIMARY_BLACK,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            val wasPreviouslyFavorite =
                                                uiState.recipeDetailUiModel.isFavorite
                                            onAction(RecipeDetailsViewModel.Action.FavClick)
                                            if (!wasPreviouslyFavorite) {
                                                showAddToFavSnackbar = true
                                            }
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                width = 1.dp,
                                                color = AppColor.NEUTRAL_LIGHT,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .background(AppColor.WHITE)
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            painter = if (uiState.recipeDetailUiModel.isFavorite.orDefault()) painterResource(
                                                R.drawable.fav_filled_icon
                                            ) else painterResource(R.drawable.fav_icon),
                                            contentDescription = "Favorite",
                                            tint = if (uiState.recipeDetailUiModel.isFavorite.orDefault()) AppColor.ROSE_COLOR else AppColor.PRIMARY_BLACK,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Gap(height = 16.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RecipeDetailsCard(
                                title = "Ready in",
                                value = uiState.recipeDetailUiModel.recipeDetailsDto.readyInMinutes.toString()
                            )

                            RecipeDetailsCard(
                                title = "Servings",
                                value = uiState.recipeDetailUiModel.recipeDetailsDto.servings.toString()
                            )

                            RecipeDetailsCard(
                                title = "Price/serving",
                                value = uiState.recipeDetailUiModel.recipeDetailsDto.pricePerServing.toString()
                            )
                        }

                        Gap(height = 32.dp)

                        Text(
                            text = "Ingredients",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = AppTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W700,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = AppColor.PRIMARY_BLACK,
                                textAlign = TextAlign.Start
                            )
                        )

                        Gap(height = 12.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            uiState
                                .recipeDetailUiModel.recipeDetailsDto.recipeIngredientDtos?.take(6)
                                ?.forEach { ingredient ->
                                    Column(
                                        modifier = Modifier.width(86.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AsyncImage(
                                            contentDescription = "Equipment",
                                            modifier = Modifier
                                                .size(86.dp)
                                                .clip(CircleShape)
                                                .background(Color.LightGray),
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(ingredient.image)
                                                .crossfade(true)
                                                .build(),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(R.drawable.recipe_icon),
                                            error = painterResource(R.drawable.recipe_icon),
                                            fallback = painterResource(R.drawable.recipe_icon)
                                        )

                                        Gap(height = 8.dp)

                                        Text(
                                            text = ingredient.name.asName(),
                                            style = AppTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.W500,
                                                fontSize = 12.sp,
                                                lineHeight = 14.sp,
                                                color = AppColor.PRIMARY_BLACK,
                                                textAlign = TextAlign.Center
                                            ),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                        }

                        Gap(32.dp)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Instructions",
                                style = AppTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.W700,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = AppColor.PRIMARY_BLACK
                                )
                            )
                            Gap(height = 12.dp)
                            Text(
                                text = parseHtmlToText(
                                    uiState.recipeDetailUiModel.recipeDetailsDto.instructions
                                        ?: ""
                                ),
                                style = AppTheme.typography.bodySmall
                                    .copy(
                                        fontWeight = FontWeight.W400,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        color = AppColor.SECONDARY,
                                    )
                            )

                            Gap(height = 32.dp)

                            Text(
                                text = "Equipments",
                                style = AppTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.W700,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = AppColor.PRIMARY_BLACK
                                )
                            )
                            Gap(height = 12.dp)
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                uiState.recipeDetailUiModel.recipeDetailsDto.recipeIngredientDtos?.take(
                                    2
                                )
                                    ?.forEach { ingredient ->
                                        Column(
                                            modifier = Modifier.width(86.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            AsyncImage(
                                                contentDescription = "Equipment",
                                                modifier = Modifier
                                                    .size(86.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.LightGray),
                                                contentScale = ContentScale.Crop,
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(ingredient.image)
                                                    .crossfade(true)
                                                    .build(),
                                                placeholder = painterResource(R.drawable.recipe_icon),
                                                error = painterResource(R.drawable.recipe_icon),
                                                fallback = painterResource(R.drawable.recipe_icon)
                                            )

                                            Gap(height = 8.dp)

                                            Text(
                                                text = ingredient.name.asName(),
                                                style = AppTheme.typography.bodySmall.copy(
                                                    fontWeight = FontWeight.W500,
                                                    fontSize = 12.sp,
                                                    lineHeight = 14.sp,
                                                    color = AppColor.PRIMARY_BLACK,
                                                    textAlign = TextAlign.Center
                                                ),
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .wrapContentHeight()
                        ) {
                            Gap(height = 32.dp)
                            Text(
                                "Quick Summary",
                                style = AppTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.W700,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = AppColor.PRIMARY_BLACK
                                )
                            )

                            Gap(height = 12.dp)
                            Text(
                                text = parseHtmlToText(
                                    uiState.recipeDetailUiModel.recipeDetailsDto.summary ?: ""
                                ),
                                style = AppTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.W400,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = AppColor.SECONDARY,
                                )
                            )

                            Gap(height = 32.dp)

                            Box(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .background(AppColor.GREY_1)
                                    .padding(16.dp)
                            ) {
                                ExpandableRow(
                                    title = "Nutrition",
                                    content = "Lorem ipsum dolor sit amet consectetur. Sagittis facilisis aliquet aenean lorem ullamcorper et. Risus lectus id sed fermentum in. At porta sed ut lorem volutpat elementum mi sollicitudin. Laoreet tempor nullam velit dui amet mauris sed ac sem."
                                )
                            }
                            Gap(height = 4.dp)

                            Box(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .background(AppColor.GREY_1)
                                    .padding(16.dp)
                            ) {
                                ExpandableRow(
                                    title = "Bad for health nutrition",
                                    content = "badNutrition"
                                )
                            }

                            Gap(height = 4.dp)

                            Box(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .background(AppColor.GREY_1)
                                    .padding(16.dp)
                            ) {
                                ExpandableRow(
                                    title = "Good for health nutrition",
                                    content = "goodNutrition"
                                )
                            }
                        }
                    }
                } ?: run {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AppColor.ORANGE
                    )
                }

                // Collapsed Toolbar - shows when scrolled
                AnimatedVisibility(
                    visible = isCollapsed.value,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300)),
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(300)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = statusBarHeight),
                        color = AppColor.WHITE,
                        shadowElevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { NavigationProvider.navController.popBackStack() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = 1.dp,
                                        color = AppColor.NEUTRAL_LIGHT,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(AppColor.WHITE)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.back_icon),
                                    contentDescription = "Back",
                                    tint = AppColor.PRIMARY_BLACK,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (showReminderBottomSheet) {
                ReminderBottomSheet(
                    onDismiss = { showReminderBottomSheet = false },
                    onReminderSet = {
                        onAction(RecipeDetailsViewModel.Action.UpdateNotificationTime(it.millis))
                        showReminderAddedSnackbar = true
                    }
                )
            }
        }
    }
}