package rajeev.ranjan.recipeapp.search.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.base_ui.AppBottomSheet
import rajeev.ranjan.recipeapp.core.base_ui.DragHandleType
import rajeev.ranjan.recipeapp.core.utils.asName
import rajeev.ranjan.recipeapp.search.module.RecipeDetailUiModel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecipeInformationSheet(
    recipeModel: RecipeDetailUiModel,
    onGetSimilarRecipe: () -> Unit,
    onFavClick: () -> Unit,
    onDismiss: () -> Unit,
) {

    var showIngredients by remember { mutableStateOf(false) }
    var showRecipe by remember { mutableStateOf(false) }

    AppBottomSheet(
        onDismiss = onDismiss,
        type = DragHandleType.BACK
    ) { state, scope ->
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            recipeModel.recipeDetailsDto.let { recipeDetails ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
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
                            text = recipeDetails.title ?: "",
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
                        if (recipeModel.isFavorite) {
                            Icon(
                                painter = painterResource(R.drawable.fav_filled_icon),
                                contentDescription = "Add to favorites",
                                tint = AppColor.ROSE_COLOR,
                                modifier = Modifier
                                    .size(16.dp)
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

                Gap(height = 16.dp)
                HorizontalDivider(thickness = 1.dp, color = AppColor.NEUTRAL_LIGHT)
            }

            LazyColumn(
                modifier = if (!showIngredients && !showRecipe) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                }
            ) {
                item {
                    SectionHeader(
                        title = "Ingredients",
                        expanded = showIngredients,
                        onToggle = { showIngredients = !showIngredients }
                    )
                }

                if (showIngredients) {
                    recipeModel.recipeDetailsDto.recipeIngredientDtos?.let { ingredients ->
                        val chunkedIngredients = ingredients.chunked(3)
                        items(chunkedIngredients) { rowIngredients ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(13.33.dp)
                            ) {
                                rowIngredients.forEach { ingredient ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        AsyncImage(
                                            model = ingredient.image,
                                            contentDescription = ingredient.name,
                                            modifier = Modifier
                                                .size(104.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    width = 1.dp,
                                                    color = AppColor.GREY_2,
                                                    shape = CircleShape
                                                ),
                                            contentScale = ContentScale.Crop
                                        )
                                        Gap(height = 8.dp)
                                        Text(
                                            text = ingredient.name,
                                            style = AppTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.W500,
                                                fontSize = 12.sp,
                                                lineHeight = 14.sp,
                                                color = AppColor.PRIMARY_BLACK,
                                                textAlign = TextAlign.Center,
                                            ),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                repeat(3 - rowIngredients.size) {
                                    Gap(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                item {
                    SectionHeader(
                        title = "Full recipe",
                        expanded = showRecipe,
                        onToggle = { showRecipe = !showRecipe }
                    )
                }

                if (showRecipe) {
                    item {
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
                                text = recipeModel.recipeDetailsDto.instructions ?: "",
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
                            FlowRow(
                                maxItemsInEachRow = 3,
                                maxLines = 3,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                recipeModel.recipeDetailsDto.recipeIngredientDtos
                                    ?.forEach { ingredient ->
                                        Column {
                                            AsyncImage(
                                                model = ingredient.image,
                                                contentDescription = "Equipment",
                                                modifier = Modifier
                                                    .size(86.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.LightGray),
                                                contentScale = ContentScale.Crop
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
                    }

                    item {
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
                                text = recipeModel.recipeDetailsDto.summary ?: "",
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
                }

                item {
                    Gap(height = 16.dp)
                }
            }

            Button(
                onClick = onGetSimilarRecipe,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColor.ORANGE
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Get Similar Recipe",
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

            // Final bottom spacing
            Gap(height = 16.dp)
        }
    }
}

@Composable
private fun SectionHeader(title: String, expanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = AppTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                color = AppColor.PRIMARY_BLACK
            )
        )
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "Expand/Collapse"
        )
    }
}

@Composable
fun ExpandableRow(title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.GREY_1)
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title, style = AppTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W700,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = AppColor.PRIMARY_BLACK,
                )
            )

            Gap(modifier = Modifier.weight(1f))

            Icon(
                imageVector = if (expanded) Icons.Filled.PlayArrow else Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = AppColor.WHITE,
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(AppColor.PRIMARY_BLACK)
                    .padding(4.dp)
                    .rotate(if (expanded) -90f else 90f)
            )
        }

        AnimatedVisibility(expanded) {
            Gap(height = 12.dp)
            Text(
                content,
                style = AppTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = AppColor.SECONDARY,
                )
            )
        }
    }
}