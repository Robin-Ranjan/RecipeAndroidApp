package rajeev.ranjan.recipeapp.search.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.AppBottomSheet
import rajeev.ranjan.recipeapp.core.DragHandleType
import rajeev.ranjan.recipeapp.core.navigation.NavigationProvider
import rajeev.ranjan.recipeapp.search.viewModel.SearchViewmodel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

@Composable
fun SearchScreenRoot(viewModel: SearchViewmodel = koinViewModel()) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

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

    var showSheet by remember { mutableStateOf(false) }
    var showIngrediant by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AppColor.WHITE,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Rtl),
                    end = paddingValues.calculateStartPadding(LayoutDirection.Ltr)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            {
//                Surface(
//                    tonalElevation = 1.dp,
//                    color = AppColor.WHITE,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = statusBarHeight)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        IconButton(onClick = { NavigationProvider.navController.popBackStack() }) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "Back"
//                            )
//                        }
//
//                        Surface(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(44.dp)
//                                .border(
//                                    width = 1.dp,
//                                    color = if (isFocused) AppColor.ORANGE else AppColor.GREY_2,
//                                    shape = RoundedCornerShape(12.dp)
//                                )
//                                .clip(RoundedCornerShape(12.dp)),
//                            color = AppColor.GREY_2,
//                            onClick = { isFocused = true }
//                        ) {
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(horizontal = 12.dp),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(
//                                    painter = painterResource(R.drawable.search_icon),
//                                    contentDescription = "Search",
//                                    tint = if (isFocused || uiState.query.isNotEmpty()) AppColor.ORANGE else AppColor.GREY_2
//                                )
//
//                                Spacer(modifier = Modifier.width(8.dp))
//
//                                BasicTextField(
//                                    value = uiState.query,
//                                    onValueChange = {
//                                        onAction(SearchViewmodel.Action.OnQueryChange(it))
//                                        isFocused = it.isNotEmpty()
//                                    },
//                                    singleLine = true,
//                                    textStyle = AppTheme.typography.bodySmall.copy(color = AppColor.PRIMARY_BLACK),
//                                    cursorBrush = SolidColor(AppColor.ORANGE),
//                                    modifier = Modifier.fillMaxWidth(),
//                                    decorationBox = { innerTextField ->
//                                        if (uiState.query.isEmpty()) {
//                                            Text(
//                                                text = "Search Any Recipe",
//                                                style = AppTheme.typography.bodySmall.copy(color = AppColor.GREY_2)
//                                            )
//                                        }
//                                        innerTextField()
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }

                SearchBar(
                    query = uiState.query,
                    onAction = onAction,
                    statusBarHeight = statusBarHeight
                )

                Gap(height = 8.dp)
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
                                    showSheet = true
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
    if (showSheet) {
        RecipeBottomSheet(
            title = "rajeev",
            image = "https://img.spoonacular.com/recipes/716429-556x370.jpg",
            readyInMinutes = "10",
            servings = "10",
            onGetIngredients = {},
            onDismiss = { showSheet = false },
            onTitleClick = { showIngrediant = true }
        )
    }

    if (showIngrediant) {
        IngredientBottomSheet(
            title = "rajeev",
            image = "https://img.spoonacular.com/recipes/716429-556x370.jpg",
            readyInMinutes = "10",
            servings = "10",
            onGetIngredients = {},
            onDismiss = { showIngrediant = false }
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onAction: (SearchViewmodel.Action) -> Unit,
    statusBarHeight: Dp
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Surface(
        tonalElevation = 1.dp,
        color = AppColor.WHITE,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = statusBarHeight)
    ) {
        // Single search bar with everything inside
        BasicTextField(
            value = query,
            onValueChange = { newQuery ->
                onAction(SearchViewmodel.Action.OnQueryChange(newQuery))
            },
            singleLine = true,
            textStyle = AppTheme.typography.bodySmall.copy(
                color = AppColor.PRIMARY_BLACK
            ),
            cursorBrush = SolidColor(AppColor.ORANGE),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 16.dp)
                .border(
                    width = 1.dp,
                    color = if (isFocused) AppColor.ORANGE else AppColor.SECONDARY,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = AppColor.WHITE,
                    shape = RoundedCornerShape(8.dp)
                )
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button INSIDE the search bar
                    Icon(
                        painter = painterResource(R.drawable.back_icon),
                        contentDescription = "Back",
                        tint = AppColor.PRIMARY_BLACK,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                NavigationProvider.navController.popBackStack()
                            }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Text content area
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = "Search Any Recipe",
                                style = AppTheme.typography.bodySmall.copy(
                                    color = AppColor.SECONDARY
                                )
                            )
                        }
                        innerTextField()
                    }

                    // Clear button INSIDE the search bar (when text exists)
                    if (query.isNotEmpty()) {
                        Icon(
                            painter = painterResource(R.drawable.cross_icon),
                            contentDescription = "Clear",
                            tint = AppColor.PRIMARY_BLACK,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    onAction(SearchViewmodel.Action.OnQueryChange(""))
                                }
                        )
                    }
                }
            }
        )
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

@Composable
fun RecipeBottomSheet(
    title: String,
    onTitleClick: () -> Unit,
    image: String,
    readyInMinutes: String,
    servings: String,
    onDismiss: () -> Unit,
    onGetIngredients: () -> Unit
) {
    AppBottomSheet(
        onDismiss = onDismiss
    ) {
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
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTitleClick() }
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

            // Recipe Image
            AsyncImage(
                model = image,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(392.dp)

            )

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
                        text = servings.toString(),
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
    ) {
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
                        text = servings.toString(),
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
