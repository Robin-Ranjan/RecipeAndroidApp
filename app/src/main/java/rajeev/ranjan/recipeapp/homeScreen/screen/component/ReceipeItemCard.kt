package rajeev.ranjan.recipeapp.homeScreen.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import rajeev.ranjan.recipeapp.core.RecipeUiModel
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme


@Composable
fun RecipeItemCard(
    item: RecipeUiModel,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = { onClick() },
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
                    .memoryCacheKey(item.image)
                    .diskCacheKey(item.image)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
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