package rajeev.ranjan.recipeapp.search.screen.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.core.base_ui.AppBottomSheet
import rajeev.ranjan.recipeapp.core.utils.orDefault
import rajeev.ranjan.recipeapp.search.module.RecipeDetailUiModel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme

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
    ) { state, scope ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            data?.let {
                val recipe = data.recipeDetailsDto
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = recipe.title.orDefault(),
                        style = AppTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AppColor.PRIMARY_BLACK
                        ),
                        modifier = Modifier
                            .weight(1f)

                    )

                    IconButton(
                        onClick = { onFavClick() },
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .border(width = 1.dp, color = AppColor.GREY_2)
                    ) {
                        if (data.isFavorite) {
                            Icon(
                                painter = painterResource(R.drawable.fav_filled_icon),
                                contentDescription = "Add to favorites",
                                tint = AppColor.ROSE_COLOR,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.fav_icon),
                                contentDescription = "Add to favorites",
                                tint = AppColor.GREY_2,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Recipe Image
                AsyncImage(
                    model = recipe.image,
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(20.dp))

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
                            text = "${recipe.readyInMinutes} min",
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
                            text = recipe.servings.toString(),
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
                            text = "₹${recipe.pricePerServing}",
                            style = AppTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AppColor.ORANGE
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
                    shape = RoundedCornerShape(12.dp)
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
            } ?: run {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}