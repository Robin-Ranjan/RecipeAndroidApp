package rajeev.ranjan.recipeapp.search.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.navigation.NavigationProvider
import rajeev.ranjan.recipeapp.search.viewModel.SearchViewmodel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme

@Composable
fun AppSearchBar(
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