package rajeev.ranjan.recipeapp.search.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.base_ui.AppBottomSheet
import rajeev.ranjan.recipeapp.core.base_ui.DragHandleType
import rajeev.ranjan.recipeapp.homeScreen.screen.component.RecipeItemCard
import rajeev.ranjan.recipeapp.search.module.SimilarRecipes
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimilarRecipesBottomSheet(
    recipesName: String,
    isFavorite: Boolean,
    isSimilarLoading: Boolean,
    data: List<SimilarRecipes>,
    onFavClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AppBottomSheet(
        onDismiss = onDismiss,
        type = DragHandleType.BACK
    ) { state, scope ->
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Ingredient & Recipe",
                        style = AppTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.W700,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            color = AppColor.PRIMARY_BLACK
                        )
                    )
                    Gap(height = 4.dp)
                    Text(
                        text = recipesName,
                        style = AppTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontStyle = FontStyle.Normal,
                            color = AppColor.SECONDARY
                        )
                    )
                }

                IconButton(
                    onClick = { onFavClick() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = AppColor.NEUTRAL_LIGHT,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    if (isFavorite) {
                        Icon(
                            painter = painterResource(R.drawable.fav_filled_icon),
                            contentDescription = "Add to favorites",
                            tint = AppColor.ROSE_COLOR,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.fav_icon),
                            contentDescription = "Add to favorites",
                            tint = AppColor.PRIMARY_BLACK,
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }
                }
            }

//            Gap(height = 16.dp)

            HorizontalDivider(thickness = 1.dp, color = AppColor.NEUTRAL_LIGHT)

            if (isSimilarLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = AppColor.ORANGE
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(data) {
                        SimilarItemCard(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun SimilarItemCard(item: SimilarRecipes) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = AppColor.WHITE,
        border = BorderStroke(width = 1.dp, color = AppColor.GREY_2)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.recipe_icon),
                error = painterResource(R.drawable.recipe_icon),
                fallback = painterResource(R.drawable.recipe_icon)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.title,
                    maxLines = 1,
                    style = AppTheme.typography.h1.copy(
                        fontSize = 14.0.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.W500,
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ready In ${item.readyInMinutes} Minutes",
                    style = AppTheme.typography.bodySmall
                )
            }
        }
    }
}