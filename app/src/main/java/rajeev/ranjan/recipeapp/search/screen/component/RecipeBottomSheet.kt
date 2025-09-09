package rajeev.ranjan.recipeapp.search.screen.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.base_ui.AppBottomSheet
import rajeev.ranjan.recipeapp.core.utils.orDefault
import rajeev.ranjan.recipeapp.search.module.RecipeDetailUiModel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeBottomSheet(
    data: RecipeDetailUiModel?,
    onFavClick: () -> Unit,
    onDismiss: () -> Unit,
    onGetIngredients: () -> Unit
) {
    AppBottomSheet(
        onDismiss = onDismiss
    ) { _, _ ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            data?.let {
                val recipe = data.recipeDetailsDto

                // Scrollable content
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = recipe.title.orDefault(),
                            style = AppTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W700,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                color = AppColor.PRIMARY_BLACK,
                                textAlign = TextAlign.Start
                            ),
                            modifier = Modifier.weight(1f)
                        )

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
                            if (data.isFavorite) {
                                Icon(
                                    painter = painterResource(R.drawable.fav_filled_icon),
                                    contentDescription = "Add to favorites",
                                    tint = AppColor.ROSE_COLOR,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.fav_icon),
                                    contentDescription = "Add to favorites",
                                    tint = AppColor.PRIMARY_BLACK,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    AsyncImage(
                        model = recipe.image,
                        contentDescription = recipe.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(392.dp)
                    )

                    Gap(height = 16.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        RecipeDetailsCard(title = "Ready in", value = recipe.readyInMinutes.toString())
                        Gap(width = 12.dp)
                        RecipeDetailsCard(title = "Servings", value = recipe.servings.toString())
                        Gap(width = 12.dp)
                        RecipeDetailsCard(title = "Price/serving", value = recipe.pricePerServing.toString())
                    }

                    Gap(height = 16.dp)
                }

                // Fixed button at bottom
                Button(
                    onClick = onGetIngredients,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColor.ORANGE),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Get Ingredients",
                        style = AppTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.W600,
                            color = AppColor.WHITE,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                        )
                    )

                    Gap(width = 13.dp)

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AppColor.WHITE,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun RecipeDetailsCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .width(112.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = AppColor.NEUTRAL_LIGHT,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Text(
            text = title,
            style = AppTheme.typography.bodySmall.copy(
                color = AppColor.SECONDARY,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            )
        )

        Gap(height = 4.dp)

        Text(
            text = "$value min",
            style = AppTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = AppColor.ORANGE
            )
        )
    }
}